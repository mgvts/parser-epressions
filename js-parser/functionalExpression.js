const variables = {
    "x": 0,
    "y": 1,
    "z": 2
}


const abstructBin = f => (a, b) => (...args) => f(a(...args), b(...args))
const add = abstructBin((a, b) => a + b)
const subtract = abstructBin((a, b) => a - b)
const multiply = abstructBin((a, b) => a * b)
const divide = abstructBin((a, b) => a / b)
const cnst = value => () => value
const variable = name => (...args) => args[variables[name]]
const abstractUno = f => a => (...args) => f(a(...args))
const negate = abstractUno(x => -x)
const sinh = abstractUno(x => Math.sinh(x))
const cosh = abstractUno(x => Math.cosh(x))

const pi = cnst(Math.PI)
const e = cnst(Math.E)
