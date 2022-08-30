
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.context
    (:require [mid.re-frame.event-vector :as event-vector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn context->event-vector
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @example
  ;  (re-frame/context->event-vector {:coeffects {:event [:my-event ...]}})
  ;  =>
  ;  [:my-event ...]
  ;
  ; @return (vector)
  [context]
  (get-in context [:coeffects :event]))

(defn context->event-id
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:event (vector)}}
  ;
  ; @example
  ;  (re-frame/context->event-vector {:coeffects {:event [:my-event ...]}})
  ;  =>
  ;  :my-event
  ;
  ; @return (keyword)
  [context]
  (-> context context->event-vector event-vector/event-vector->event-id))

(defn context->db-before-effect
  ; @param (map) context
  ;  {:coeffects (map)
  ;   {:db (map)}}
  ;
  ; @example
  ;  (re-frame/context->db-before-effect {:coeffects {:db {...}}})
  ;  =>
  ;  {...}
  ;
  ; @return (map)
  [context]
  (get-in context [:coeffects :db]))

(defn context->db-after-effect
  ; @param (map) context
  ;  {:effects (map)
  ;   {:db (map)}}
  ;
  ; @example
  ;  (re-frame/context->db-before-effect {:effects {:db {...}}})
  ;  =>
  ;  {...}
  ;
  ; @return (map)
  [context]
  (get-in context [:effects :db]))
