
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.routes.subs
    (:require [plugins.plugin-handler.routes.subs  :as routes.subs]
              [plugins.view-selector.transfer.subs :as transfer.subs]
              [x.app-core.api                      :refer [r]]
              [x.app-router.api                    :as router]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def get-extended-route routes.subs/get-extended-route)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ;
  ; @return (keyword)
  [db [_ _]]
  (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
          (keyword derived-view-id)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-view-route
  ; @param (keyword) selector-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (r view-selector/get-view-route :my-selector :my-view)
  ;  =>
  ;  "/@app-home/my-selector/my-view"
  ;
  ; @return (string)
  [db [_ selector-id view-id]]
  (r get-extended-route db selector-id (name view-id)))
