
(ns components.vector-item-list.views
    (:require [components.illustration.views          :as illustration.views]
              [components.vector-item-list.attributes :as vector-item-list.attributes]
              [components.vector-item-list.prototypes :as vector-item-list.prototypes]
              [fruits.random.api                      :as random]
              [pretty-elements.api                    :as pretty-elements]
              [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vector-item-list-placeholder
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:placeholder (map)(opt)
  ;   {}}
  [_ {{:keys [illustration label]} :placeholder}]
  [:div {:class :c-vector-item-list--placeholder}
        (if illustration [illustration.views/component {:illustration illustration :height :s :width :s}])
        (if label        [pretty-elements/label {:content label :font-size :l :font-weight :semi-bold}])])

(defn- vector-item-list-items
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  ; {:item-element (Reagent component symbol)
  ;  :value-path (Re-Frame path vector)}
  [list-id {:keys [item-element value-path] :as list-props}]
  (letfn [(f0 [items item-dex _] (conj items [item-element item-dex]))]
         (let [items @(r/subscribe [:get-item value-path])]
              (if (empty? items)
                  [vector-item-list-placeholder list-id list-props]
                  (reduce-kv f0 [:<>] items)))))

(defn- vector-item-list
  ; @ignore
  ;
  ; @param (keyword) list-id
  ; @param (map) list-props
  [list-id list-props]
  [:div (vector-item-list.attributes/list-attributes list-id list-props)
        [:div (vector-item-list.attributes/list-body-attributes list-id list-props)
              [vector-item-list-items list-id list-props]]])

(defn component
  ; @param (keyword)(opt) list-id
  ; @param (map) list-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :item-element (Reagent component symbol)
  ;  :outdent (map)(opt)
  ;   {:bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :placeholder (map)(opt)
  ;   {:illustration (string)(opt)
  ;    :label (metamorphic-content)(opt)}
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)}
  ;
  ; @usage
  ; [vector-item-list {...}]
  ;
  ; @usage
  ; [vector-item-list :my-vector-item-list {...}]
  ;
  ; @usage
  ; (defn my-item-element [item-dex] ...)
  ; [vector-item-list :my-vector-item-list {:item-element #'my-item-element
  ;                                         :value-path   [:my-items]}]
  ([list-props]
   [component (random/generate-keyword) list-props])

  ([list-id list-props]
   ; @note (tutorials#parametering)
   (fn [_ list-props]
       (let [] ; list-props (vector-item-list.prototypes/list-props-prototype list-props)
            [vector-item-list list-id list-props]))))
