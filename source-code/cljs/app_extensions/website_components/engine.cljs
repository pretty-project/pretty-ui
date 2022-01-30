
(ns app-extensions.website-components.engine
    (:require [x.app-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:environment/add-external-css! "/css/x/website-components.css"]})
