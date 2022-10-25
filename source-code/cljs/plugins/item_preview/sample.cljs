
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-preview.sample
    (:require [plugins.item-preview.api :as item-preview]
              [re-frame.api             :as r]
              [x.app-elements.api       :as elements]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:item-preview/init-preview! ...] eseményt használni!



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-preview
  []
  [item-preview/body :my-preview {:item-id         "my-item"
                                  :preview-element [:div "My preview"]}])



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-preview engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-preview/body :my-preview {:item-id         "my-item"
                                  :preview-element [:div "My preview"]
                                  :query           [:my-query]}])
