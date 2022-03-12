
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.view-selector.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn transfer-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/transfer-id :my-extension)
  ;  =>
  ;  :my-extension.view-selector/transfer-selector-props
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (str (name extension-id) ".view-selector/transfer-selector-props")))

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-id :my-extension)
  ;  =>
  ;  :my-extension.view-selector/route
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (str (name extension-id) ".view-selector/route")))

(defn extended-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/extended-route-id :my-extension)
  ;  =>
  ;  :my-extension.view-selector/extended-route
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (str (name extension-id) ".view-selector/extended-route")))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-template :my-extension)
  ;  =>
  ;  "/my-extension"
  ;
  ; @return (string)
  [extension-id]
  (str "/@app-home/" (name extension-id)))

(defn extended-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/extended-route-template :my-extension)
  ;  =>
  ;  "/my-extension/:view-id"
  ;
  ; @return (string)
  [extension-id]
  (str "/@app-home/" (name extension-id)
       "/:view-id"))

(defn route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-string :my-extension)
  ;  =>
  ;  "/@app-home/my-extension"
  ;
  ; @return (string)
  [extension-id]
  (str "/@app-home/" (name extension-id)))

(defn extended-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (engine/extended-route-string :my-extension :my-view)
  ;  =>
  ;  "/@app-home/my-extension/my-view"
  ;
  ; @return (string)
  [extension-id view-id]
  (str "/@app-home/" (name extension-id)
       "/"           (name view-id)))

(defn component-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) component-key
  ;
  ; @example
  ;  (engine/component-id :my-extension :view)
  ;  =>
  ;  :my-extension.view-selector/view
  ;
  ; @return (keyword)
  [extension-id component-key]
  ; XXX#5467
  (keyword (str (name extension-id) ".view-selector")
           (name component-key)))

(defn load-extension-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/load-extension-event :my-extension)
  ;  =>
  ;  [:my-extension.view-selector/load-selector!]
  ;
  ; @return (event-vector)
  [extension-id]
  (let [event-id (keyword (str (name extension-id) ".view-selector/load-selector!"))]
       [event-id]))
