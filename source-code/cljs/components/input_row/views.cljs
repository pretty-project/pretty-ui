
(ns components.input-row.views
    (:require ;[components.input-row.prototypes :as input-row.prototypes]
              [elements.api                     :as elements]
              [hiccup.api :as hiccup]
              [random.api                       :as random]
              [x.components.api                 :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- input-row-cell-label
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; @param (map) cell-props
  ; {}
  [row-id _ {:keys [label target-id]}]
  [:label.c-input-cell--label {:data-selectable false
                               :data-font-size :xs
                               :data-color :muted
                               :data-font-weight :bold
                               :data-indent-left :xs
                               ;:data-indent-horizontal :xxs
                               :data-line-height :block
                               :for (hiccup/value target-id "input")}
                              [x.components/content row-id label]])

(defn- input-row-cell
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; @param (map) cell-props
  ; {}
  [row-id row-props {:keys [input template] :as cell-props}]
  [:div.c-input-cell {:style {:grid-template-columns template}
                      :data-outdent-horizontal :xxs}
                     [input-row-cell-label row-id row-props cell-props]
                     [x.components/content row-id input]])

(defn- input-row-cells
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {}
  [row-id {:keys [cells] :as row-props}]
  (letfn [(f [cells cell] (conj cells [input-row-cell row-id row-props cell]))]
         (reduce f [:<>] cells)))

(defn- input-row
  ; @param (keyword) row-id
  ; @param (map) row-props
  ; {}
  [row-id {:keys [template] :as row-props}]
  [:div.c-input-row {}
                    [:div.c-input-row--body {:data-border :bottom
                                             :style {:grid-template-columns template}}
                                            [input-row-cells row-id row-props]]])

(defn component
  ; @param (keyword)(opt) row-id
  ; @param (map) row-props
  ; {:border (keyword)
  ;   :both, :bottom, :top
  ;  :cells (maps in vector)
  ;   [{:label (metamorphic-content)
  ;     :input (metamorphic-content)
  ;     :target-id (keyword)(opt)
  ;     :template (string)(opt)}]
  ;  :template (string)}
  ;
  ; @usage
  ; [input-row {...}]
  ;
  ; @usage
  ; [input-row :my-input-row {...}]
  ([row-props]
   [component (random/generate-keyword) row-props])

  ([row-id row-props]
   (let [] ; row-props (input-row.prototypes/row-props-prototype row-props)
        [input-row row-id row-props])))
