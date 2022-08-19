

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.views
    (:require [dom.api                             :as dom]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-environment.api               :as environment]
              [x.app-tools.infinite-loader.helpers :as infinite-loader.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  [loader-id]
  (let [observer-id (infinite-loader.helpers/loader-id->observer-id loader-id)
        hidden?     (a/subscribe [:tools/infinite-observer-hidden? loader-id])]
       (fn [] [:div {:id    (a/dom-value observer-id)
                     :style (if (deref hidden?)
                                {:position :fixed :bottom "-100px"})}])))

(defn- infinite-loader
  ; @param (keyword) loader-id
  [loader-id]
  [:div.x-infinite-loader {:id (a/dom-value  loader-id)}
                          [infinite-observer loader-id]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:on-viewport (metamorphic-event)}
  ;
  ; @usage
  ;  [tools/infinite-loader {:on-viewport ...}]
  ([loader-props]
   [component (a/id) loader-props])

  ([loader-id {:keys [on-viewport] :as loader-props}]
   (let [observer-id (infinite-loader.helpers/loader-id->observer-id loader-id)
         element-id  (a/dom-value observer-id)
         callback-f #(if % (a/dispatch on-viewport))]
        (reagent/lifecycles {:component-did-mount    (fn [] (environment/setup-intersection-observer!  element-id callback-f))
                             :component-will-unmount (fn [] (environment/remove-intersection-observer! element-id))
                             :reagent-render         (fn [] [infinite-loader loader-id])}))))
