
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.sample
    (:require [plugins.item-picker.api :as item-picker]
              [re-frame.api            :as r]
              [x.app-elements.api      :as elements]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-picker/init-picker! ...] eseményt használni!



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-picker
  []
  [item-picker/body :my-picker {:preview-element [:div "My preview"]}])



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-picker plugin body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve kerül elküldésre.
(defn my-query
  []
  [item-picker/body :my-picker {:preview-element [:div "My preview"]
                                :query           [:my-query]}])
