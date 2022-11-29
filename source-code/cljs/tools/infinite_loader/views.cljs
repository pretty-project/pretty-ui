
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.infinite-loader.views
    (:require [dom.api                       :as dom]
              [hiccup.api                    :as hiccup]
              [random.api                    :as random]
              [re-frame.api                  :as r]
              [reagent.api                   :as reagent]
              [tools.infinite-loader.helpers :as helpers]
              [x.environment.api             :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  [loader-id]
  (let [observer-id (helpers/loader-id->observer-id loader-id)
        hidden?     (r/subscribe [:infinite-loader/observer-hidden? loader-id])]
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
  ;  [component {:on-viewport ...}]
  ([loader-props]
   [component (random/generate-keyword) loader-props])

  ([loader-id {:keys [on-viewport] :as loader-props}]
   (let [observer-id (helpers/loader-id->observer-id loader-id)
         element-id  (hiccup/value observer-id)
         callback-f #(if % (r/dispatch on-viewport))]
        (reagent/lifecycles {:component-did-mount    (fn [] (x.environment/setup-intersection-observer!  element-id callback-f))
                             :component-will-unmount (fn [] (x.environment/remove-intersection-observer! element-id))
                             :reagent-render         (fn [] [infinite-loader loader-id])}))))
