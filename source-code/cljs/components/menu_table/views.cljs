
(ns components.menu-table.views
    (:require [components.menu-table.helpers    :as menu-table.helpers]
              [components.menu-table.prototypes :as menu-table.prototypes]
              [fruits.random.api                :as random]
              [fruits.vector.api                :as vector]
              [pretty-elements.api              :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- table-header
  ; @return (maps in vector)
  []
  [[{:content :label :font-size :xs :horizontal-align :center :indent {:right :xs}}
    {:content :link  :font-size :xs :horizontal-align :center :indent {:right :xs}}
    {:content :open! :font-size :xs :horizontal-align :center}]])

(defn- menu-item-label-props
  ; @param (integer) row-dex
  ; @param (map) item-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [row-dex {:keys [label]}]
  {:outdent (if (zero? row-dex) {:top :xxs :right :xs} {:top :xs :right :xs})
   :content label :color :muted :text-selectable? true
   :style {:min-width "120px" :background-color "var(--fill-color-highlight)"
           :padding "6px 12px" :border-radius "var(--border-radius-s)"}})

(defn- menu-item-target-props
  ; @param (integer) row-dex
  ; @param (map) item-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [row-dex {:keys [target]}]
  {:outdent (if (zero? row-dex) {:top :xxs} {:top :xs})
   :color :muted :content (case target :self :in-self-page :blank :in-blank-page nil)
   :style {:min-width "200px" :background-color "var(--fill-color-highlight)"
           :padding "6px 12px" :border-radius "var(--border-radius-s)"}})

(defn- menu-item-link-props
  ; @param (integer) row-dex
  ; @param (map) item-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [row-dex {:keys [link]}]
  {:outdent (if (zero? row-dex) {:top :xxs :right :xs} {:top :xs :right :xs})
   :content link :color :muted :text-selectable? true :copyable? true :marked? true
   :style {:min-width "200px" :background-color "var(--fill-color-highlight)"
           :padding "6px 12px" :border-radius "var(--border-radius-s)"}})

(defn- menu-item-list
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:items (maps in vector)}
  [table-id {:keys [items] :as table-props}]
  (letfn [(f0 [rows row-dex item-props]
              (conj rows [(menu-item-label-props  row-dex item-props)
                          (menu-item-link-props   row-dex item-props)
                          (menu-item-target-props row-dex item-props)]))]))
         ;[data-table.views/component {:rows (vector/concat-items (table-header)
          ;                                                       (reduce-kv f0 [] items))]))]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items-placeholder
  ; @param (keyword) table-id
  ; @param (map) table-props
  [_ _]
  [:div.c-menu-table--placeholder [pretty-elements/label {:color     :highlight
                                                          :content   :no-items-to-show
                                                          :font-size :xs}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-table-label
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:label (metamorphic-content)}
  [_ {:keys [label]}]
  (if label [:div.c-menu-table--label [pretty-elements/label {:content label}]]))

(defn- menu-table-body
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:items (maps in vector)(opt)}
  [table-id {:keys [items] :as table-props}]
  [:div.c-menu-table--inner (menu-table.helpers/table-inner-attributes table-id table-props)
                            (if (empty? items)
                                [menu-items-placeholder table-id table-props]
                                [menu-item-list         table-id table-props])])

(defn- menu-table
  ; @param (keyword) table-id
  ; @param (map) table-props
  [table-id table-props]
  [:div.c-menu-table (menu-table.helpers/table-attributes table-id table-props)
                     [menu-table-label                    table-id table-props]
                     [menu-table-body                     table-id table-props]])

(defn view
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)}
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :items (maps in vector)(opt)
  ;  [{:label (metamorphic-content)(opt)
  ;    :link (string)(opt)
  ;    :target (keyword)
  ;     :blank, :self}]
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :xxx-placeholder (metamorphic-content)(opt)
  ;   Default: :no-items-to-show
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [menu-table {...}]
  ;
  ; @usage
  ; [menu-table :my-menu-table {...}]
  ;
  ; @usage
  ; [menu-table {:rows [[{:content "Row #1"   :font-weight :semi-bold}
  ;                      {:content "Value #1" :color :muted}
  ;                      {:content "Value #2" :color :muted}]
  ;                     [{...} {...}]]}]
  ([table-props]
   [view (random/generate-keyword) table-props])

  ([table-id table-props]
   ; @note (tutorials#parameterizing)
   (fn [_ table-props]
       (let [table-props (menu-table.prototypes/table-props-prototype table-props)]
            [menu-table table-id table-props]))))
