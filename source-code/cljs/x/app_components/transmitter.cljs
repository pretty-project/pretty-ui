
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.7.0
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.transmitter
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#0001 ----------------------------------------------------------------

; @name transmitter
;  A transmitter komponens valósítja meg az x.app-components modul komponenseinek
;  paraméterezését.
;
; @name dynamic-props
;  A base-props, initial-props és subscribed-props térképek XXX#0069 logika
;  szerinti összefésülése.
;  A {:base-props {...}} tulajdonságként átadott térképre fésüli előbb az
;  {:initial-props {...}} tulajdonságként átadott térképet, majd a
;  {:subscribed-props {...}} tulajdonságként átadott térképet, amint az nem
;  üres térképként adódik át.
;
; @name base-props
;  Az XXX#0069 logika szerinti összefésülés alapjául szolgáló térkép, amelyet
;  paraméterként az átadás után is lehetséges módosítani.
;
; @name initial-props
;  A komponens React-fába történő csatolásakor annak az {:initial-props-path [...]}
;  tulajdonságaként átadott Re-Frame adatbázis útvonalra írt térkép, ami az XXX#0069 logika
;  szerint adódik át a komponens számára, addig a pillanatig, amíg subscribed-props
;  értéke nil.
;
; @name subscribed-props
;  XXX#7081
;
; @name modifier
;  Opcionális függvény, amely az XXX#0069 logika szerint összefésült
;  dynamic-props térképet módosítja.



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-props->dynamic-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :subscribed-props (map)(opt)}
  ;
  ; @return (map)
  [component-id {:keys [base-props initial-props modifier subscribed-props]}]

  ;; -- The truth is out there ---------------
  ;; -----------------------------------------

  ; XXX#0069
  (let [dynamic-props (merge base-props (or subscribed-props initial-props))]
       (if modifier   (modifier component-id dynamic-props)
                      (return                dynamic-props))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;    {:disabled? (boolean)(opt)}
  ;   :component (component)(opt)
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)(opt)
  ;   :subscribed-props (map)(opt)}
  ;
  ; @usage
  ;  (defn my-component [component-id dynamic-props])
  ;  (defn my-modifier [component-id dynamic-props] (do-something-with dynamic-props))
  ;  [components/transmitter {:render-f         #'my-component
  ;                           :base-props       {...}
  ;                           :initial-props    {...}
  ;                           :modifier         my-modifier
  ;                           :subscribed-props {...}}]
  ([context-props]
   [component (a/id) context-props])

  ([component-id {:keys [component render-f] :as context-props}]
   (if-let [dynamic-props (context-props->dynamic-props component-id context-props)]
           (cond render-f  [render-f component-id dynamic-props]
                 component (conj     component    dynamic-props))
           (cond render-f  [render-f component-id]
                 component (return   component)))))
