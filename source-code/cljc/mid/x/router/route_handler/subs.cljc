
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.x.router.route-handler.subs
    (:require [string.api     :as string]
              [mid.x.core.api :as x.core]
              [re-frame.api   :as r :refer [r]]
              [uri.api        :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-home
  ; @example
  ;  (r get-app-home db)
  ;  =>
  ;  "/my-app"
  ;
  ; @return (string)
  [db _]
  (let [app-home (r x.core/get-app-config-item db :app-home)]
       (uri/valid-path app-home)))

(defn use-app-home
  ; @param (string) uri
  ;
  ; @example
  ;  (r use-app-home db "/@app-home/my-route")
  ;  =>
  ;  "/my-app/my-route"
  ;
  ; @example
  ;  (r use-app-home db "https://my-app.com/@app-home/my-route")
  ;  =>
  ;  "https://my-app.com/my-app/my-route"
  ;
  ; @return (string)
  [db [_ uri]]
  (let [app-home (r get-app-home db)]
       (string/replace-part uri #"/@app-home" app-home)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-domain
  ; @example
  ;  (r get-app-domain db)
  ;  =>
  ;  "https://my-app.com"
  ;
  ; @return (string)
  [db _]
  (let [app-domain (r x.core/get-app-config-item db :app-domain)]
       (uri/valid-uri app-domain)))

(defn use-app-domain
  ; @param (string) uri
  ;
  ; @example
  ;  (r use-app-domain db "/my-route")
  ;  =>
  ;  "https://my-app.com/my-route"
  ;
  ; @example
  ;  (r use-app-domain db "https://my-app.com/my-route")
  ;  =>
  ;  "https://my-app.com/my-route"
  ;
  ; @return (string)
  [db [_ uri]]
  (let [app-domain (r get-app-domain db)
        local-uri  (uri/uri->local-uri uri)]
       (str app-domain (string/starts-with! local-uri "/"))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.router/get-app-home]
(r/reg-sub :x.router/get-app-home get-app-home)

; @usage
;  [:x.router/use-app-home "/@app-home/my-route"]
(r/reg-sub :x.router/use-app-home use-app-home)

; @usage
;  [:x.router/get-app-domain]
(r/reg-sub :x.router/get-app-domain get-app-domain)

; @usage
;  [:x.router/use-app-domain "/my-route"]
(r/reg-sub :x.router/use-app-domain use-app-domain)
