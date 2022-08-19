

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.shield.side-effects
    (:require [mid-fruits.time         :as time]
              [x.app-core.api          :as a]
              [x.app-environment.api   :as environment]
              [x.app-ui.renderer       :as renderer]
              [x.app-ui.shield.helpers :as shield.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-shield!
  ; @param (*) content
  ;
  ; @usage
  ;  (ui/set-shield! "My content")
  [content]
  (environment/set-element-content! "x-app-shield--content" content)
  (if (shield.helpers/shield-hidden?)
      (environment/reveal-element-animated! "x-app-shield")))

(defn remove-shield!
  ; @usage
  ;  (ui/remove-shield!)
  []
  (if (shield.helpers/shield-visible?)
      (environment/hide-element-animated! renderer/HIDE-ANIMATION-TIMEOUT "x-app-shield"))
  (time/set-timeout! renderer/HIDE-ANIMATION-TIMEOUT (environment/empty-element! "x-app-shield--content")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-shield! "My content"]
(a/reg-fx :ui/set-shield! set-shield!)

; @usage
;  [:ui/remove-shield!]
(a/reg-fx :ui/remove-shield! remove-shield!)
