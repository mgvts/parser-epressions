function AbstrElements(evaluate, toString, prefix) {
    function Abstr(...data) {
        this._data = data;
    }

    Abstr.prototype.evaluate = evaluate;
    Abstr.prototype.toString = toString;
    Abstr.prototype.prefix = prefix;
    return Abstr;
}

const Const = AbstrElements(
    function () {
        return this._data[0]
    },
    function () {
        return this._data[0] + ""
    },
    function () {
        return this._data.toString()
    }
)

const variables = {
    "x": 0,
    "y": 1,
    "z": 2
}

const Variable = AbstrElements(
    function (...args) {
        return args[variables[this._data]]
    },
    function () {
        return this._data.toString()
    },
    function () {
        return this._data.toString()
    }
)

function AbstrOperation(computation, operaionSymbol) {
    function Operation(...args) {
        AbstractArithmeticOperation.call(this, ...args)
    }

    Operation.prototype = Object.create(AbstractArithmeticOperation.prototype)
    Operation.prototype._operaionSymbol = operaionSymbol
    Operation.prototype.computation = computation
    return Operation;
}

const AbstractArithmeticOperation = AbstrElements(
    function (...args) {
        return this.computation(...this._data.map(expression => expression.evaluate(...args)))
    },
    function () {
        return this._data.join(" ") + " " + this._operaionSymbol
    },
    function () {
        return "(" + this._operaionSymbol + " " + this._data.map(ex => ex.prefix()).join(" ") + ")"
    },
)


const Add = AbstrOperation((x, y) => x + y, "+")
const Subtract = AbstrOperation((x, y) => x - y, "-")
const Multiply = AbstrOperation((x, y) => x * y, "*")
const Divide = AbstrOperation((x, y) => x / y, "/")
const Negate = AbstrOperation((x) => -x, "negate")
const Sinh = AbstrOperation((x) => Math.sinh(x), "sinh")
const Cosh = AbstrOperation((x) => Math.cosh(x), "cosh")


const Mean = AbstrOperation(function (...args) {
    return args.reduce((x, y) => x + y, 0) / arguments.length
}, "mean")

const Var = AbstrOperation(function (...args) {
    const mean1 = args.reduce((x, y) => x + y * y, 0) / arguments.length
    const mean2 = args.reduce((x, y) => x + y, 0) / arguments.length

    return mean1 - mean2 * mean2
}, "var")

const operations1 = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
}

const unary = {
    "negate": Negate,
    "sinh": Sinh,
    "cosh": Cosh,
}

const operations = {
    "+": [Add, 2],
    "- ": [Subtract, 2],
    "-(": [Subtract, 2],
    "*": [Multiply, 2],
    "/": [Divide, 2],
    "mean": [Mean, -1],
    "var": [Var, -1],
    "negate": [Negate, 1],
    "sinh": [Sinh, 1],
    "cosh": [Cosh, 1],
}


const digits = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"]

function parse(exp) {
    function isunary() {
        for (const un of Object.keys(unary)) {
            if (exp.startsWith(un, pos)) {
                return un
            }
        }
        return false
    }

    let arr = []
    let pos = 0
    while (pos < exp.length) {
        if (exp[pos] === "-" && exp[pos + 1] in digits) {
            let num = "-"
            pos++
            while (exp[pos] in digits) {
                num += exp[pos++]
            }
            arr.push(new Const(parseInt(num)))
            continue
        }
        if (isunary()) {
            let impl = arr.pop()
            arr.push(new unary[isunary()](impl))
            pos += isunary().length
            continue
        }
        if (exp[pos] in operations1) {
            let impl = arr.pop()
            arr.push(new operations1[exp[pos++]](arr.pop(), impl))
            continue
        }
        if (exp[pos] in variables) {
            arr.push(new Variable(exp[pos++]))
            continue
        }
        if (exp[pos] in digits) {
            let num = ""
            while (exp[pos] in digits) {
                num += exp[pos++]
            }
            arr.push(new Const(parseInt(num)))
            continue
        }
        pos++
    }
    return arr[0]
}

function parsePrefix(exp) {
    let pos = 0
    let balance = 0
    skipWhiteSpace()

    function isOper() {
        for (const un of Object.keys(operations)) {
            if (exp.startsWith(un, pos)) {
                return un
            }
        }
        return false
    }

    function checkBackBracket() {
        if (exp[pos] !== ")") {
            throw new Error("no )")
        } else {
            balance--
            pos++
        }
    }

    function parseOperation() {
        let char = isOper()

        let args = []
        if (char === "-(") {
            pos++
        } else {
            pos += char.length
        }
        let operation = operations[char]
        if (operation[1] !== -1) {
            for (let i = 0; i < operation[1]; i++) {
                args.push(parseOper())
                skipWhiteSpace()
            }
        } else {
            while (exp[pos] !== ")") {
                args.push(parseOper())
                skipWhiteSpace()
            }
        }
        skipWhiteSpace()
        return new operation[0](...args)
    }

    function parseBracket() {
        skipWhiteSpace()
        if (isOper()) {
            let res = parseOperation()
            checkBackBracket()
            return res
        }
        if (exp[pos] === "(") {
            pos++
            balance++
            return parseBracket()
        }
        throw new Error("no compare in bracket")
    }

    function parseOper() {
        skipWhiteSpace()
        let unarChar = isOper()
        if (unarChar && operations[unarChar][1] === 1) {
            pos++
            return new operations[unarChar](parseOper())
        }
        if (exp[pos] in variables) {
            return new Variable(exp[pos++])
        }
        if (exp[pos] === "-" && exp[pos + 1] in digits) {
            let num = "-"
            pos++
            while (exp[pos] in digits) {
                num += exp[pos++]
            }
            return new Const(parseInt(num))
        }
        if (exp[pos] in digits) {
            let num = ""
            while (exp[pos] in digits) {
                num += exp[pos++]
            }
            return new Const(parseInt(num))
        }

        if (exp[pos] === "(") {
            pos++
            balance++
            return parseBracket()
        }
        throw new Error('no compare in Element')
    }

    function skipWhiteSpace() {
        while (pos < exp.length && exp[pos] === " ") {
            pos++
        }
    }

    let res
    let impl = isOper()
    if (impl) {
        res = parseOperation()
    } else if (exp[pos] === "(") {
        pos++
        balance++
        res = parseBracket()
    } else {
        res = parseOper()
    }
    skipWhiteSpace()

    if (pos !== exp.length) {
        throw new Error("no eof")
    } else if (balance !== 0) {
        throw new Error("bad with brackets")
    } else {
        return res
    }
}
