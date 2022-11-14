
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.15
; Description:
; Version: v0.4.4
; Compatibility: x4.5.2



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.engine)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn data-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) key
  ;
  ; @example
  ;  (data-key :my-renderer :data-items)
  ;  =>
  ;  :my-renderer/data-items
  ;
  ; @return (keyword)
  [renderer-id key]
  (keyword (name renderer-id) (name key)))

(defn renderer-id->dom-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ;
  ; @example
  ;  (renderer-id->dom-id :my-renderer)
  ;  =>
  ;  :x-app-my-renderer
  ;
  ; @return (keyword)
  [renderer-id]
  (keyword (str "x-app-" (name renderer-id))))
