
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.sample
    (:require [engines.file-editor.api :as file-editor]
              [re-frame.api            :as r]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; Az engine beállításához mindenképpen szükséges a szerver-oldali
; [:file-editor/init-editor! ...] eseményt használni!



;; -- Az engine használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-editor
  []
  [file-editor/body :my-editor {:form-element [:div "My form"]}])



;; -- A {:default-content {...}} tulajdonság használata -----------------------
;; ----------------------------------------------------------------------------

; ...



;; -- Pathom lekérés használata az elem letöltésekor --------------------------
;; ----------------------------------------------------------------------------

; A file-editor engine body komponensének {:query [...]} tulajdonságaként
; átadott Pathom lekérés vektor az elem letöltődésekor küldött lekéréssel
; összefűzve elküldésre kerül.
(defn my-query
  []
  [file-editor/body :my-editor {:form-element [:div "My form"]
                                :query        [:my-query]}])
