
(ns extensions.products.views
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-details      :as details]
              [x.app-elements.api :as elements]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id]
  [:div])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :products/render-product-list!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:x.app-ui/set-surface! ::view {:content #'view}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:extensions/add-item-list-route! "products" "product"]})
