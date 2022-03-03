
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.effects
    (:require [x.server-core.api :as a :refer [r]]
              [server-plugins.item-browser.engine :as engine]
              [server-plugins.item-browser.events :as events]
              [server-plugins.item-lister.effects :refer [lister-props-prototype]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-ITEMS-KEY :items)

; @constant (keyword)
(def DEFAULT-LABEL-KEY :name)

; @constant (keyword)
(def DEFAULT-PATH-KEY :path)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:items-key (keyword)
  ;   :label-key (keyword)
  ;   :path-key (keyword)
  ;   :routed? (boolean)}
  [extension-id item-namespace browser-props]
  (merge {:items-key DEFAULT-ITEMS-KEY
          :label-key DEFAULT-LABEL-KEY
          :path-key  DEFAULT-PATH-KEY
          :routed?   true}
         (lister-props-prototype extension-id item-namespace browser-props)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/init-browser!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:collection-name (string)(opt)
  ;    Default: (name extension-id)
  ;   :download-limit (integer)(opt)
  ;    Default: item-lister/DEFAULT-DOWNLOAD-LIMIT
  ;   :items-key (keyword)(opt)
  ;    Default: DEFAULT-ITEMS-KEY
  ;   :label-key (keyword)(opt)
  ;    Default: DEFAULT-LABEL-KEY
  ;   :order-by-options (namespaced keywords in vector)(opt)
  ;    Default: item-lister/DEFAULT-ORDER-BY-OPTIONS
  ;   :path-key (keyword)(opt)
  ;    Default: DEFAULT-PATH-KEY
  ;   :root-item-id (string)(opt)
  ;   :routed? (boolean)(opt)
  ;    Default: true
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: item-lister/DEFAULT-SEARCH-KEYS}
  ;
  ; @usage
  ;  [:item-browser/init-browser! :my-extension :my-type {:root-item-id "my-item"
  ;                                                       :search-keys  [:name :email-address]}]
  (fn [{:keys [db]} [_ extension-id item-namespace browser-props]]
      (let [browser-props (browser-props-prototype extension-id item-namespace browser-props)]
           {:db (r events/init-browser! db extension-id item-namespace browser-props)
            :dispatch-n [[:item-browser/reg-transfer-browser-props! extension-id item-namespace browser-props]
                         [:item-browser/reg-transfer-lister-props!  extension-id item-namespace browser-props]
                         [:item-browser/add-route!                  extension-id item-namespace browser-props]
                         [:item-browser/add-extended-route!         extension-id item-namespace browser-props]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/reg-transfer-browser-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  (fn [_ [_ extension-id item-namespace browser-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace :browser)
                                {:data-f (fn [_] (select-keys browser-props engine/BROWSER-PROPS-KEYS))
                                 :target-path [extension-id :item-browser/meta-items]}]}))

(a/reg-event-fx
  :item-browser/reg-transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  (fn [_ [_ extension-id item-namespace browser-props]]
      {:fx [:core/reg-transfer! (engine/transfer-id extension-id item-namespace :lister)
                                {:data-f      (fn [_] (select-keys browser-props engine/LISTER-PROPS-KEYS))
                                 :target-path [extension-id :item-lister/meta-items]}]}))

(a/reg-event-fx
  :item-browser/add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id item-namespace {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/route-id extension-id item-namespace)
                                      {:route-template (engine/route-template       extension-id item-namespace)
                                       :client-event   [:item-browser/load-browser! extension-id item-namespace]
                                       :restricted?    true}])))

(a/reg-event-fx
  :item-browser/add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:routed? (boolean)}
  (fn [_ [_ extension-id item-namespace {:keys [routed?]}]]
      (if routed? [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                                      {:route-template (engine/extended-route-template extension-id item-namespace)
                                       :client-event   [:item-browser/load-browser!    extension-id item-namespace]
                                       :restricted?    true}])))
