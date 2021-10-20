(ns extensions.clients.views
  (:require
   [plugins.item-lister.api :as item-lister]
   [x.app-elements.api :as elements]
   [x.app-core.api :as a]
   [x.app-ui.api :as ui]))



;; ----------------------------------------------------------------------------
;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item [lister-id item-dex {:car/keys [name model id]}]
  [:<> [elements/label {:content (str name " " model)}]
       [elements/horizontal-line {}]])


(defn- view
  []
  [:div])

;; ----------------------------------------------------------------------------
;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; ----------------------------------------------------------------------------
;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------


(a/reg-event-fx
  :x.app-extensions.clients/render!
  [:x.app-ui/set-surface!
   ::view
   {:content #'item-lister/items
    :content-props {:value-path [:a-items]
                    :download-items-event [:clients/download-clients!]
                    :element #'client-item
                    :label   :clients}
    :label-bar {:content #'ui/go-home-surface-label-bar
                :content-props {:label :clients}}
    :control-bar {:content #'item-lister/search-bar}}])


(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot
   {:dispatch-n [[:x.app-router/add-route!
                  ::route
                  {:restricted?    true
                   :route-event    [:x.app-extensions.clients/render!]
                   :route-template "/clients"}]
                 [:x.app-router/add-route!
                  ::extended-route
                  {:restricted?    true
                   :route-event    [:x.app-extensions.clients/render!]
                   :route-template "/clients/:client-id"}]]}})

;; ----------------------------------------------------------------------------
;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------
