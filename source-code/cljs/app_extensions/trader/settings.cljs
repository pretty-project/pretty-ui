
(ns app-extensions.trader.settings
    (:require [mid-fruits.map     :refer [dissoc-in]]
              [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-extensions.trader.styles :as styles]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [settings-id]
  [:div {:style (styles/settings-style)}])

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id]
  [settings view-id])
