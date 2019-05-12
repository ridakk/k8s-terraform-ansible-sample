const express = require('express')
const app = express()
const port = 8050

app.get('/healthz', (req, res) => res.send('ok'))

app.listen(port, () => console.log(`Example app listening on port ${port}!`))
