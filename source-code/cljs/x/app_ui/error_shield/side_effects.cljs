
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.error-shield.side-effects
    (:require [re-frame.api                  :as r]
              [time.api                      :as time]
              [x.app-environment.api         :as environment]
              [x.app-ui.error-shield.helpers :as error-shield.helpers]
              [x.app-ui.renderer             :as renderer]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-shield!
  ; @param (*) content
  ;
  ; @usage
  ;  (set-error-shield! "My content")
  [content]
  (environment/set-element-content! "x-error-shield--content" content)
  (if (error-shield.helpers/error-shield-hidden?)
      (environment/reveal-element-animated! "x-error-shield")))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-error-shield! "My content"]
(r/reg-fx :ui/set-error-shield! set-error-shield!)
