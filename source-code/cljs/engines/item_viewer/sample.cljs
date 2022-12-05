
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-viewer.sample
    (:require [engines.item-viewer.api :as item-viewer]
              [re-frame.api            :as r]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:item-viewer/init-viewer! ...] eseményt használni!



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-viewer
  []
  [item-viewer/body :my-viewer {:item-element [:div "My item"]}])




;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------

; ...



;; -- Az [:item-viewer/view-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(r/reg-event-fx :view-my-item!
  [:item-viewer/view-item! :my-viewer "my-item"])



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; Az item-viewer engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [item-viewer/body :my-viewer {:item-element [:div "My item"]
                                :query        [:my-query]}])
