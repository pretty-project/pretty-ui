
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



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
(def get-extended-route  routes.subs/get-extended-route)
(def get-derived-view-id routes.subs/get-derived-view-id)



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
