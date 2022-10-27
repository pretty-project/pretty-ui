
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.clerk.side-effects
    (:require [clerk.core]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn initialize!
  ; @usage
  ;  (initialize!)
  [_]
  (clerk.core/initialize!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn navigate-page!
  ; @param (string) route-string
  ;
  ; @usage
  ;  (navigate-page! "my-route#my-fragment")
  [route-string]
  (println "clerk/navigate-page!" route-string)
  (clerk.core/navigate-page! route-string))

(defn after-render!
  ; @usage
  ;  (after-render!)
  []
  (println "clerk/after-render!")
  (clerk.core/after-render!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:clerk/initialize!]
(r/reg-fx :clerk/initialize! initialize!)

; @usage
;  [:clerk/navigate-page!]
(r/reg-fx :clerk/navigate-page! navigate-page!)

; @usage
;  [:clerk/after-render!]
(r/reg-fx :clerk/after-render! after-render!)
