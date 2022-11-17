
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.components.delayer.views
    (:require [random.api                   :as random]
              [reagent.api                  :as reagent]
              [x.components.content.views   :as content.views]
              [x.components.delayer.helpers :as delayer.helpers]
              [x.components.delayer.state   :as delayer.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- delayer-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  ;  {:placeholder (metamorphic-content)(opt)}
  [delayer-id {:keys [placeholder]}]
  (if placeholder [content.views/component delayer-id {:content placeholder}]))

(defn- delayer-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  ;  {:content (metamorphic-content)}
  [delayer-id {:keys [content]}]
  [content.views/component delayer-id {:content content}])

(defn- delayer-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  [delayer-id delayer-props]
  (if (get-in @delayer.state/DELAYERS [delayer-id :time-elapsed?])
      [delayer-content     delayer-id delayer-props]
      [delayer-placeholder delayer-id delayer-props]))

(defn- delayer
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) delayer-id
  ; @param (map) delayer-props
  [delayer-id delayer-props]
  (reagent/lifecycles {:component-did-mount    (fn [] (delayer.helpers/delayer-did-mount    delayer-id delayer-props))
                       :component-will-unmount (fn [] (delayer.helpers/delayer-will-unmount delayer-id delayer-props))
                       :reagent-render         (fn [] [delayer-body                         delayer-id delayer-props])}))

(defn component
  ; @param (keyword)(opt) delayer-id
  ; @param (map) delayer-props
  ;  {:content (metamorphic-content)
  ;   :placeholder (metamorphic-content)(opt)
  ;   :timeout (ms)}
  ;
  ; @usage
  ;  [delayer {...}]
  ;
  ; @usage
  ;  [delayer :my-delayer {...}]
  ;
  ; @usage
  ;  [delayer :my-delayer {:content     [:div "My content"]
  ;                        :placeholder [:div "My placeholder"]
  ;                        :timeout     3000}]
  ([delayer-props]
   [component (random/generate-keyword) delayer-props])

  ([delayer-id delayer-props]
   [delayer delayer-id delayer-props]))
