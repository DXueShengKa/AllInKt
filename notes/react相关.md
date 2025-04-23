
## useState
- useState 必须在 React 函数组件或自定义 Hook 中调用。
- 不能在普通函数、条件语句、循环中调用 Hook。
- 这些规则是为了保证 Hook 的调用顺序一致，从而确保状态管理的正确性。如果违反这些规则，React 会在开发模式下抛出错误提示。
- *由于useState函数必须位于react中，view model这种方式不适合在react中使用*
