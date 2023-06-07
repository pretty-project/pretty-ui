
(ns components.menu-table.views
    (:require ;[components.data-table.views      :as data-table.views]
              [components.menu-table.helpers    :as menu-table.helpers]
              [components.menu-table.prototypes :as menu-table.prototypes]
              [elements.api                     :as elements]
              [random.api                       :as random]
              [vector.api                       :as vector]))

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
  {:outdent (if (= 0 row-dex) {:top :xxs :right :xs} {:top :xs :right :xs})
   :content label :color :muted :selectable? true
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
  {:outdent (if (= 0 row-dex) {:top :xxs} {:top :xs})
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
  {:outdent (if (= 0 row-dex) {:top :xxs :right :xs} {:top :xs :right :xs})
   :content link :color :muted :selectable? true :copyable? true :marked? true
   :style {:min-width "200px" :background-color "var(--fill-color-highlight)"
           :padding "6px 12px" :border-radius "var(--border-radius-s)"}})

(defn- menu-item-list
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:items (maps in vector)}
  [table-id {:keys [items] :as table-props}]
  (letfn [(f [rows row-dex item-props]
             (conj rows [(menu-item-label-props  row-dex item-props)
                         (menu-item-link-props   row-dex item-props)
                         (menu-item-target-props row-dex item-props)]))]))
         ;[data-table.views/component {:rows (vector/concat-items (table-header)
          ;                                                       (reduce-kv f [] items))]))]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-items-placeholder
  ; @param (keyword) table-id
  ; @param (map) table-props
  [_ _]
  [:div.c-menu-table--placeholder [elements/label {:color     :highlight
                                                   :content   :no-items-to-show
                                                   :font-size :xs}]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-table-label
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:label (metamorphic-content)}
  [_ {:keys [label]}]
  (if label [:div.c-menu-table--label [elements/label {:content     label
                                                       :selectable? false}]]))

(defn- menu-table-body
  ; @param (keyword) table-id
  ; @param (map) table-props
  ; {:items (maps in vector)(opt)}
  [table-id {:keys [items] :as table-props}]
  [:div.c-menu-table--body (menu-table.helpers/table-body-attributes table-id table-props)
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

(defn component
  ; @param (keyword)(opt) table-id
  ; @param (map) table-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)}
  ;  :indent (map)(opt)
  ;  :items (maps in vector)(opt)
  ;  [{:label (metamorphic-content)(opt)
  ;    :link (string)(opt)
  ;    :target (keyword)
  ;     :blank, :self}]
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;  :placeholder (metamorphic-content)(opt)
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
   [component (random/generate-keyword) table-props])

  ([table-id table-props]
   (let [table-props (menu-table.prototypes/table-props-prototype table-props)]
        [menu-table table-id table-props])))
