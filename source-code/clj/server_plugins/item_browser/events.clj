
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.8
; Compatibility: x4.5.5



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.events
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.map    :as map]
              [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-browser.events    :as events]
              [server-plugins.item-browser.engine :as engine]
              [server-plugins.item-browser.subs   :as subs]
              [server-plugins.item-lister.api     :as item-lister]
              [server-plugins.item-lister.events  :refer [lister-props-prototype]]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-LABEL-KEY :name)

; @constant (keyword)
(def DEFAULT-PATH-KEY :path)

; @constant (keywords in vector)
(def BROWSER-PROPS-KEYS [:label :label-key :path-key :root-item-id :routed?])

; @constant (keywords in vector)
(def LISTER-PROPS-KEYS [:download-limit :label :order-by :order-by-options :search-keys])



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-browser.events
(def store-browser-props! events/store-browser-props!)
(def store-lister-props!  events/store-lister-props!)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  ;  {:label-key (keyword)
  ;   :path-key (keyword)
  ;   :routed? (boolean)}
  [extension-id item-namespace browser-props]
  (merge {:label-key DEFAULT-LABEL-KEY
          :path-key  DEFAULT-PATH-KEY
          :routed?   true}
         (lister-props-prototype extension-id item-namespace browser-props)))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  [db [_ extension-id item-namespace browser-props]]
  (let [lister-props  (select-keys browser-props LISTER-PROPS-KEYS)
        browser-props (select-keys browser-props BROWSER-PROPS-KEYS)]
       (as-> db % (r store-lister-props!  % extension-id item-namespace lister-props)
                  (r store-browser-props! % extension-id item-namespace browser-props))))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-browser-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:core/reg-transfer! {:data-f (fn [_] (select-keys browser-props BROWSER-PROPS-KEYS))
                        :target-path [extension-id :item-browser/meta-items]}])

(defn transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:core/reg-transfer! {:data-f      (fn [_] (select-keys browser-props LISTER-PROPS-KEYS))
                        :target-path [extension-id :item-lister/meta-items]}])

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:routed? (boolean)}
  [_ [_ extension-id item-namespace {:keys [routed?]}]]
  (if routed? [:router/add-route! (engine/route-id extension-id item-namespace)
                                  {:route-template (engine/route-template       extension-id)
                                   :client-event   [:item-browser/load-browser! extension-id item-namespace]
                                   :restricted?    true}]))

(defn add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:routed? (boolean)}
  [_ [_ extension-id item-namespace {:keys [routed?]}]]
  (if routed? [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                                  {:route-template (engine/extended-route-template extension-id)
                                   :client-event   [:item-browser/load-browser!    extension-id item-namespace]
                                   :restricted?    true}]))

(a/reg-event-fx
  :item-browser/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:download-limit (integer)(opt)
  ;    Default: item-lister/DEFAULT-DOWNLOAD-LIMIT
  ;   :label (metamorphic-content)(opt)
  ;    Default: extension-id
  ;   :label-key (keyword)(opt)
  ;    Default: DEFAULT-LABEL-KEY
  ;   :order-by (keyword)(opt)
  ;    Default: item-lister/DEFAULT-ORDER-BY
  ;   :order-by-options (keywords in vector)(opt)
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
  ;  [:item-browser/initialize! :my-extension :my-type {:root-item-id "my-item"
  ;                                                     :search-keys  [:name :email-address]}]
  (fn [{:keys [db] :as cofx} [_ extension-id item-namespace browser-props]]
      (let [browser-props (browser-props-prototype extension-id item-namespace browser-props)]
           {:db (r initialize! db extension-id item-namespace browser-props)
            :dispatch-n [(r transfer-browser-props! cofx extension-id item-namespace browser-props)
                         (r transfer-lister-props!  cofx extension-id item-namespace browser-props)
                         (r add-route!              cofx extension-id item-namespace browser-props)
                         (r add-extended-route!     cofx extension-id item-namespace browser-props)]})))
