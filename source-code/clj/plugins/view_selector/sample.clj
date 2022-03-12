
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.sample
    (:require [plugins.view-selector.api :as view-selector]
              [x.server-core.api         :as a]))



;; -- A plugin beállítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; ...
(a/reg-lifecycles!
  ::lifecycles
  {:on-server-boot [:view-selector/init-selector! :my-extension
                                                  {:route-template "/@app-home/my-extension/:view-id"}]})
