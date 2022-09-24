
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.views
    (:require [dom.api                       :as dom]
              [mid-fruits.hiccup             :as hiccup]
              [mid-fruits.random             :as random]
              [reagent.api                   :as reagent]
              [tools.infinite-loader.helpers :as helpers]
              [x.app-core.api                :as a]
              [x.app-environment.api         :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  [loader-id]
  (let [observer-id (helpers/loader-id->observer-id loader-id)
        hidden?     (a/subscribe [:infinite-loader/observer-hidden? loader-id])]
       (fn [] [:div {:id    (hiccup/value observer-id)
                     :style (if (deref hidden?)
                                {:position :fixed :bottom "-100px"})}])))

(defn- infinite-loader
  ; @param (keyword) loader-id
  [loader-id]
  [:div.infinite-loader {:id (hiccup/value loader-id)}
                        [infinite-observer loader-id]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:on-viewport (metamorphic-event)}
  ;
  ; @usage
  ;  [infinite-loader/component {:on-viewport ...}]
  ([loader-props]
   [component (random/generate-keyword) loader-props])

  ([loader-id {:keys [on-viewport] :as loader-props}]
   (let [observer-id (helpers/loader-id->observer-id loader-id)
         element-id  (hiccup/value observer-id)
         callback-f #(if % (a/dispatch on-viewport))]
        (reagent/lifecycles {:component-did-mount    (fn [] (environment/setup-intersection-observer!  element-id callback-f))
                             :component-will-unmount (fn [] (environment/remove-intersection-observer! element-id))
                             :reagent-render         (fn [] [infinite-loader loader-id])}))))
