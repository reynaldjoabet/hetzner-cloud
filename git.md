```sh
git worktree list
/Projects/hetzner-cloud  565fad0 [main]
```

All worktrees share:
- commits
- branches
- tags
- remotes

Each worktree has:
- its own checked-out branch
- its own working files

```sh
git worktree list
/path/repo              abc123 [main]
/path/repo-hotfix       def456 [hotfix]
/path/repo-feature-x    ghi789 [feature-x]
```

### git worktree add
Creates a new worktree.
Add worktree with an existing branch
```sh
git worktree add ../feature-login feature-login
git worktree add <path>, <branch-or-commit>
```

Add worktree and create a new branch
```sh
git worktree add -b feature-api ../api-work main
git worktree add -b feature-api ../api-work feature-api
```
`-b feature-api` → create branch
`../api-work `→ folder
`main `→ branch to base it on..`feature-api` will start from `main`

Add worktree at a specific commit (detached HEAD)
`git worktree add ../test-old abc123`

### git worktree remove
Removes a worktree directory safely.
```sh
git worktree remove <path>
git worktree remove ../feature-login
```

### git worktree prune
Cleans up stale or deleted worktrees.
`git worktree prune`
Useful when:
A worktree folder was deleted manually
Git still thinks it exists

### git worktree move
Moves a worktree to a new location.
`git worktree move ../feature-login ../feature-login-renamed`

```sh
git worktree add -b feature-a ../feature-a
git worktree add -b feature-b ../feature-b
```

Hotfix without disturbing main work
`git worktree add ../hotfix hotfix`
cd ../hotfix

Review a PR locally
```sh
git fetch origin
git worktree add ../pr-123 origin/pr-123
```

`git worktree COMMAND [OPTIONS] PATH [BRANCH | COMMIT]`