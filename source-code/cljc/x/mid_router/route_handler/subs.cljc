
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-router.route-handler.subs
    (:require [mid-fruits.string :as string]
              [mid-fruits.uri    :as uri]
              [x.mid-core.api    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
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

(defn use-app-home
  ; @param (string) uri
  ;
  ; @example
  ;  (r router/use-app-home db "/@app-home/my-route")
  ;  =>
  ;  "/my-app/my-route"
  ;
  ; @return (string)
  [db [_ uri]]
  (let [app-home (r get-app-home db)]
       (string/replace-part uri #"/@app-home" app-home)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:router/get-app-home]
(a/reg-sub :router/get-app-home get-app-home)
