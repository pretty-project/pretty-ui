
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.23
; Description:
; Version: v0.9.4
; Compatibility: x4.5.6



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.subscriber
    (:require [mid-fruits.random            :as random]
              [x.app-components.transmitter :rename {component transmitter}]
              [x.app-core.api               :as a]))



;; -- Names -------------------------------------------------------------------
;; -- XXX#7081 ----------------------------------------------------------------

; @name subscriber
;  A {:subscriber [...]} tulajdonságként átadott Re-Frame subscription vektor
;  használatával a subscriber komponens feliratkozik a subscription visszatérési
;  értékére, és azt {:subscribed-props {...}} térképként a XXX#0069 logika
;  szerint átadja a komponensnek.
;
; @name initial-props
;  XXX#0001
;
; @name base-props
;  XXX#0001
;
; @name modifier
;  XXX#0001



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:base-props (map)(opt)
  ;   :component (component)(opt)
  ;    Only w/o {:render-f ...}
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)(opt)
  ;    Only w/o {:component ...}
  ;   :subscriber (subscription-vector)
  ;    A visszatérési értéknek térkép típusnak kell lennie!}
  ;
  ; @usage
  ;  [components/subscriber {...}]
  ;
  ; @usage
  ;  [components/subscriber :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id component-props])
  ;  [components/subscriber :my-component
  ;                         {:render-f   #'my-component
  ;                          :subscriber [:get-my-component-props]}]
  ;
  ; @return (*)
  ([context-props]
   [component (random/generate-keyword) context-props])

  ([component-id {:keys [subscriber] :as context-props}]
   (let [subscribed-props (a/subscribe subscriber)]
        (fn [_ context-props]
            (let [context-props (assoc context-props :subscribed-props @subscribed-props)]
                 [transmitter component-id context-props])))))
