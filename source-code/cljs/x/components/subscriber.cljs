
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

(ns x.components.subscriber
    (:require [random.api               :as random]
              [re-frame.api             :as r]
              [x.components.transmitter :rename {component transmitter}]))



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
  ;    W/O {:render-f ...}
  ;   :initial-props (map)(opt)
  ;   :modifier (function)(opt)
  ;   :render-f (function)(opt)
  ;    W/O {:component ...}
  ;   :subscriber (subscription-vector)
  ;    A visszatérési értéknek térkép típusnak kell lennie!}
  ;
  ; @usage
  ;  [subscriber {...}]
  ;
  ; @usage
  ;  [subscriber :my-component {...}]
  ;
  ; @usage
  ;  (defn my-component [component-id component-props])
  ;  [subscriber :my-component
  ;              {:render-f   #'my-component
  ;               :subscriber [:get-my-component-props]}]
  ;
  ; @return (*)
  ([context-props]
   [component (random/generate-keyword) context-props])

  ([component-id {:keys [subscriber] :as context-props}]
   (let [subscribed-props (r/subscribe subscriber)]
        (fn [_ context-props]
            (let [context-props (assoc context-props :subscribed-props @subscribed-props)]
                 [transmitter component-id context-props])))))
