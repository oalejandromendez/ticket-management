const { FlatCompat } = require('@eslint/eslintrc');
const pluginJs = require('@eslint/js');
const classic = require('./.eslintrc.cjs');

const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: pluginJs.configs.recommended,
});

module.exports = [...compat.config(classic)];
