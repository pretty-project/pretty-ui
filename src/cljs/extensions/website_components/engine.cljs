
(ns extensions.website-components.engine
    (:require [x.app-core.api :as a]))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:x.app-environment.css-handler/add-external-source! "css/x/website-components.css"]})
