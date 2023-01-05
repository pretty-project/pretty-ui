
(ns tools.infinite-loader.views
    (:require [hiccup.api                    :as hiccup]
              [random.api                    :as random]
              [re-frame.api                  :as r]
              [reagent.api                   :as reagent]
              [tools.infinite-loader.helpers :as helpers]
              [tools.infinite-loader.state   :as state]
              [x.environment.api             :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- infinite-observer
  ; @param (keyword) loader-id
  ; @param (boolean) hidden?
  [loader-id]
  (let [observer-id (helpers/loader-id->observer-id loader-id)]
       (fn [] [:div {:id    (hiccup/value observer-id)
                     :style (if (helpers/observer-paused? loader-id)
                                {:position "fixed" :bottom "-100px"})}])))

(defn- infinite-loader
  ; @param (keyword) loader-id
  [loader-id]
  [:div.infinite-loader {:id (hiccup/value loader-id)}
                        [infinite-observer loader-id]])

(defn component
  ; @param (keyword)(opt) loader-id
  ; @param (map) loader-props
  ; {:on-intersect (metamorphic-event)(opt)
  ;  :on-leave (metamorphic-event)(opt)}
  ;
  ; @usage
  ; [component {:on-intersect [:my-event]
  ;             :on-leave     [:your-event]}]
  ([loader-props]
   [component (random/generate-keyword) loader-props])

  ([loader-id {:keys [on-intersect on-leave] :as loader-props}]
   (let [observer-id (helpers/loader-id->observer-id loader-id)
         element-id  (hiccup/value observer-id)
         callback-f  (fn [%] (swap! state/OBSERVERS assoc-in [loader-id :intersect?] %)
                             (r/dispatch (if % on-intersect on-leave)))]
        (reagent/lifecycles {:component-did-mount    (fn [] (x.environment/setup-intersection-observer!  element-id callback-f))
                             :component-will-unmount (fn [] (x.environment/remove-intersection-observer! element-id)
                                                            (swap! state/OBSERVERS dissoc loader-id))
                             :reagent-render         (fn [] [infinite-loader loader-id])}))))
