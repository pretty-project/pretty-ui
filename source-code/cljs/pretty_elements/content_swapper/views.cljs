
(ns pretty-elements.content-swapper.views
    (:require [fruits.hiccup.api                          :as hiccup]
              [fruits.random.api                          :as random]
              [metamorphic-content.api                    :as metamorphic-content]
              [pretty-elements.content-swapper.prototypes :as content-swapper.prototypes]
              [pretty-elements.content-swapper.attributes :as content-swapper.attributes]
              [pretty-elements.content-swapper.state      :as content-swapper.state]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [re-frame.api                               :as r]
              [react.api                                  :as react]
              [reagent.api                                :as reagent]

              [fruits.mixed.api :as mixed]))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn put-with-indexed
  ; @description
  ; Conjugates the items of the given 'n' collection to the given 'container' tag while applying
  ; the given 'put-f' function on each item and providing the item index to the function.
  ;
  ; @param (keyword)(opt) container
  ; Default: [:div]
  ; @param (collection) n
  ; @param (function) put-f
  ;
  ; @usage
  ; (defn my-put-f [dex %] (conj % dex "X"))
  ; (put-with-indexed [[:span "A"] [:span "B"]] my-put-f)
  ; =>
  ; [:div [:span "A" 0 "X"] [:span "B" 1 "X"]]
  ;
  ; @usage
  ; (defn my-put-f [dex %] (conj % dex "X"))
  ; (put-with-indexed [:ul] [[:li "A"] [:li "B"]] my-put-f)
  ; =>
  ; [:ul [:li "A" 0 "X"] [:li "B" 1 "X"]]
  ;
  ; @return (hiccup)
  ([n f]
   (put-with-indexed [:div] n f))

  ([container n put-f]
   (if (vector? container)
       (let [n     (mixed/to-seqable n)
             put-f (mixed/to-ifn put-f)]
            (letfn [(f0 [container dex x]
                        (if x (conj container ^{:key (:id x)} [:div (put-f dex x)
                                                                    "key:" (:id x) "k"])
                              (->   container)))]
                   (reduce-kv f0 container (vec n)))))))

(defn a
  [swapper-id id content]
  (reagent/lifecycles {:should-component-update (fn [& abc] (println abc))
                       :reagent-render (fn [swapper-id id content] (println "rerending" id)
                                           (let [active-content (-> @content-swapper.state/SWAPPERS swapper-id :active-content)]
                                                [react/mount-animation {:mounted? (= id active-content)}
                                                                       [:div {:class :pe-content-swapper--content}
                                                                             [metamorphic-content/compose content]]]))}))

(defn- content-swapper
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  [swapper-id swapper-props]
  [:div (content-swapper.attributes/swapper-attributes swapper-id swapper-props)
        [:div (content-swapper.attributes/swapper-body-attributes swapper-id swapper-props)
              (let [content-pool   (-> @content-swapper.state/SWAPPERS swapper-id :content-pool)
                    active-content (-> @content-swapper.state/SWAPPERS swapper-id :active-content)]
                   (letfn [(f0 [dex {:keys [id content]}] [react/mount-animation {:mounted? (= id active-content)}
                                                                                 [:div {:class :pe-content-swapper--content}
                                                                                       [metamorphic-content/compose content]
                                                                                       (str id active-content "xx")]])
                           (f1 [dex {:keys [id content]}] [a swapper-id id content])]
                       [:div
                        [:> react/transition-group
                            (hiccup/put-with-indexed [:<>] content-pool f0 :id)]
                        (str content-pool)]))]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- element-lifecycles
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ; {:initial-content (metamorphic-content)(opt)}
  [swapper-id {:keys [initial-content] :as swapper-props}]
  (let [initial-state {:content-pool [{:id :initial-content :content initial-content}] :active-content :initial-content}]
       ; @note (tutorials#parametering)
       (reagent/lifecycles {:component-did-mount    (fn [_ _] (swap! content-swapper.state/SWAPPERS update swapper-id merge initial-state))
                            :component-will-unmount (fn [_ _] (swap! content-swapper.state/SWAPPERS dissoc swapper-id))
                            :reagent-render         (fn [_ swapper-props] [content-swapper swapper-id swapper-props])})))
                            ; + element-did-mount, element-will-unmount

(defn element
  ; @important
  ; XXX#0517
  ; The 'content-swapper' element's content has absolute positioning.
  ; Therefore, the 'content-swapper' element and its body are stretched to their
  ; parents in order to clear space for the content because it is not doing it
  ; for itself (because its absolute positioning).
  ;
  ; @param (keyword)(opt) swapper-id
  ; @param (map) swapper-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initial-content (metamorphic-content)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)
  ;
  ; @usage
  ; [content-swapper {...}]
  ;
  ; @usage
  ; [content-swapper :my-content-swapper {...}]
  ([swapper-props]
   [element (random/generate-keyword) swapper-props])

  ([swapper-id swapper-props]
   ; @note (tutorials#parametering)
   (fn [_ swapper-props]
       (let [swapper-props (pretty-presets.engine/apply-preset                 swapper-id swapper-props)
             swapper-props (content-swapper.prototypes/swapper-props-prototype swapper-id swapper-props)]
            [element-lifecycles swapper-id swapper-props]))))
