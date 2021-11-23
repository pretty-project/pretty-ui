
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.16
; Description:
; Version: v0.6.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.transmitter
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [x.app-core.api     :as a]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#0001 ----------------------------------------------------------------

; @name transmitter
;  A transmitter komponens valósítja meg az x.app-components modul komponenseinek
;  paraméterezési logikáját.
;  Az egyes komponensek számára első paraméterként átadja a komponens azonosítóját,
;  utolsó paraméterként az ún. dynamic-props térképet, esetlegesen második
;  paraméterként pedig a {:static-props {...}} tulajdonságként átadott térképet.
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
;  A komponens csatolásakor annak az {:initial-props-path [...]} tulajdonságaként
;  átadott Re-Frame adatbázis útvonalra rögzített térkép, ami az XXX#0069 logika
;  szerint adódik át a komponens számára, addig a pillanatig, amíg subscribed-props
;  értéke nil.
;
; @name subscribed-props
;  XXX#7081
;
; @name modifier
;  Opcionális függvény, amely az XXX#0069 logika szerint összefésült
;  dynamic-props térképet módosítja.
;
; @name static-props
;  A komponens számára paraméterként változatlanul átadott térkép.



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
  (let [dynamic-props (merge (param base-props)
                             (or    subscribed-props initial-props))]
       (if (fn?      modifier)
           (modifier component-id dynamic-props)
           (return   dynamic-props))))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- debug
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) static-props
  ; @param (map) dynamic-props
  ;
  ; @return (hiccup)
  [component-id static-props dynamic-props]
  [:div {:id    (keyword/to-dom-value component-id "debug")
         :style {:display :none}}
        (str "component-id: "  component-id)
        (str "static-props: "  static-props)
        (str "dynamic-props: " dynamic-props)])

(defn component
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;    {:disabled? (boolean)(opt)}
  ;   :component (component)
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :static-props (map)(opt)
  ;   :subscribed-props (map)(opt)}
  ;
  ; @usage
  ;  (defn my-component [component-id static-props])
  ;  [components/transmitter {:component    #'my-component
  ;                           :static-props {...}}]
  ;
  ; @usage
  ;  (defn my-component [component-id dynamic-props])
  ;  [components/transmitter {:component        #'my-component
  ;                           :base-props       {...}
  ;                           :initial-props    {...}
  ;                           :subscribed-props {...}}]
  ;
  ; @usage
  ;  (defn my-modifier [component-id dynamic-props] (do-something-with dynamic-props))
  ;  (defn my-component [component-id static-props dynamic-props])
  ;  [components/transmitter {:component        #'my-component
  ;                           :base-props       {...}
  ;                           :initial-props    {...}
  ;                           :modifier         my-modifier
  ;                           :static-props     {...}
  ;                           :subscribed-props {...}}]
  ;
  ; @return (component)
  ([context-props]
   [component (a/id) context-props])

  ([component-id {:keys [component static-props] :as context-props}]
   (let [dynamic-props (context-props->dynamic-props component-id context-props)]
                   ; Both static-props and dynamic-props
        [:<> (cond (and (map/nonempty? static-props)
                        (map/nonempty? dynamic-props)) [component component-id static-props dynamic-props]
                   ; Only static-props
                   (map/nonempty? static-props)        [component component-id static-props]
                   ; Only dynamic-props
                   (map/nonempty? dynamic-props)       [component component-id dynamic-props]
                   ; *
                   :else                               [component component-id])])))

             ; DEBUG
             ;[debug component-id static-props dynamic-props]
