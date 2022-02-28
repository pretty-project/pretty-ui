
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.infinite-loader.views
    (:require [app-fruits.dom     :as dom]
              [app-fruits.reagent :as reagent]
              [x.app-core.api     :as a]
              [x.app-tools.infinite-loader.engine :as infinite-loader.engine]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  [loader-id]
  (let [observer-id (infinite-loader.engine/loader-id->observer-id loader-id)
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
   (let [observer-id  (infinite-loader.engine/loader-id->observer-id loader-id)
         element-id   (a/dom-value observer-id)
         callback-f  #(a/dispatch  on-viewport)]
        (reagent/lifecycles {:component-did-mount (fn [] (dom/setup-intersection-observer! element-id callback-f))
                             :reagent-render      (fn [] [infinite-loader loader-id])}))))
