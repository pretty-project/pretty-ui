
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.12.17
; Description:
; Version: v0.4.0
; Compatibility: x4.6.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.route-handler
    (:require [mid-fruits.candy    :refer [param return]]
              [mid-fruits.string   :as string]
              [mid-fruits.uri      :as uri]
              [x.mid-core.api      :as a :refer [r]]
              [x.mid-router.engine :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-home
  ; @example
  ;  (r router/get-app-home db)
  ;  =>
  ;  "/my-app"
  ;
  ; @return (string)
  [db _]
  (let [app-home (r a/get-app-config-item db :app-home)]
       (uri/valid-path app-home)))

(defn get-resolved-uri
  ; @param (route-string) uri
  ;
  ; @example
  ;  (r router/get-resolved-uri db "/@app-home/my-route")
  ;  =>
  ;  "/my-app/my-route"
  ;
  ; @return (string)
  [db [_ uri]]
  (let [app-home (r get-app-home db)]
       (string/replace-part uri #"/@app-home" app-home)))
