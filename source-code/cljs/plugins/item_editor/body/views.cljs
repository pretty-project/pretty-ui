
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.body.views
    (:require [plugins.item-editor.body.prototypes :as body.prototypes]
              [plugins.item-editor.core.helpers    :as core.helpers]
              [mid-fruits.string                   :as string]
              [reagent.api                         :as reagent]
              [x.app-core.api                      :as a]
              [x.app-elements.api                  :as elements]
              [x.app-layouts.api                   :as layouts]))



;; -- Form components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-namespace @(a/subscribe [:item-editor/get-transfer-item editor-id :item-namespace])
        new-item-label  (core.helpers/new-item-label editor-id item-namespace)]
       [elements/label ::new-item-label
                       {:color       :highlight
                        :content     new-item-label
                        :font-size   :l
                        :font-weight :extra-bold}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  [editor-id {:keys [name]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? editor-id])
        color             (if editor-disabled? :highlight :default)]
       [elements/label ::named-item-label
                       {:color       color
                        :content     name
                        :font-size   :l
                        :font-weight :extra-bold}]))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [item-namespace    @(a/subscribe [:item-editor/get-transfer-item editor-id :item-namespace])
        unnamed-item-label (core.helpers/unnamed-item-label editor-id item-namespace)]
       [elements/label ::unnamed-item-label
                       {:color       :highlight
                        :content     unnamed-item-label
                        :font-size   :l
                        :font-weight :extra-bold}]))

(defn item-label
  ; @param (keyword) editor-id
  ; @param (map) element-props
  ;  {:name (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/item-label :my-editor {...}]
  [editor-id {:keys [name] :as element-props}]
  ; Az [item-label ...] komponens használatához szükséges a szótárhoz adni ...
  ; ... a  {:new-my-type     {...}} kifejezést!
  ; ... az {:unnamed-my-type {...}} kifejezést!
  (let [new-item? @(a/subscribe [:item-editor/new-item? editor-id])]
       (cond (string/nonempty? name) [named-item-label   editor-id element-props]
             (boolean new-item?)     [new-item-label     editor-id]
             :unnamed-item           [unnamed-item-label editor-id])))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [elements/label ::downloading-item-label
                  {:color       :highlight
                   :content     :downloading...
                   :font-size   :xs
                   :font-weight :bold
                   :indent      {:top :xxl}}]

  ; TEMP
  [:div {:style {:width "100%"}}
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        ;[:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
        ;      [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
        ;      [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "24px" :flex-grow 1}}]]
        [:div {:style {:display "flex" :width "100%" :grid-column-gap "24px" :padding "12px 24px" :justify-content "center"}}
              [:div {:style {:background "var( --hover-color-highlight )" :border-radius "var(--border-radius-s)" :height "72px" :width "50%"}}]]])
  ; TEMP

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [downloading-item-label editor-id])
  ;[elements/row ::downloading-item
  ;              {:content          [downloading-item-label editor-id]
  ;               :horizontal-align :center])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-occured-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [_]
  [elements/label ::error-occured-label
                  {:color     :warning
                   :content   :an-error-occured
                   :font-size :m
                   :indent    {:top :xxl}}])

(defn may-be-broken-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [_]
  [elements/label ::may-be-broken-label
                  {:color   :muted
                   :content :the-item-you-opened-may-be-broken}])

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  [:<> [error-occured-label editor-id]
       [may-be-broken-label editor-id]])

(defn form-element
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  (let [form-element @(a/subscribe [:item-editor/get-body-prop editor-id :form-element])]
       [form-element editor-id]))

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  [editor-id]
  ; A) ...
  ;
  ; B) ...
  ;
  ; C) ...
  ;
  ; D) XXX#0506
  ;    A downloading-item komponens már akkor megjelenik, amikor még az [:item-editor/body-props-stored? ...]
  ;    feliratkozás visszatérési értéke FALSE. Így az item-editor plugin betöltésekor a body komponens
  ;    React-fába csatolódása és az [:item-editor/body-props-stored? ...] feliratkozás visszatérési értékének
  ;    TRUE értére változása közötti pillanatban is látható.
  (cond ; A)
        @(a/subscribe [:item-editor/get-meta-item editor-id :error-mode?])
         [error-body editor-id]
        ; B)
        @(a/subscribe [:item-editor/body-props-stored? editor-id])
         (if-let [data-received? @(a/subscribe [:item-editor/data-received? editor-id])]
                 [form-element     editor-id]
                 [downloading-item editor-id])
        ; C)
        :body-props-not-stored-yet
        [downloading-item editor-id]))

(defn body
  ; @param (keyword) editor-id
  ; @param (map) body-props
  ;  {:auto-title? (boolean)(opt)
  ;    Default: false
  ;   :form-element (metamorphic-content)
  ;   :initial-item (map)(opt)
  ;   :item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate, :revert, :save]
  ;   :item-id (string)(opt)
  ;   :item-path (vector)(opt)
  ;    Default: core.helpers/default-item-path
  ;   :new-item-id (string)(opt)
  ;   :suggestion-keys (keywords in vector)(opt)
  ;   :suggestions-path (vector)(opt)
  ;    Default: core.helpers/default-suggestions-path}
  ;
  ; @usage
  ;  [item-editor/body :my-editor {...}]
  ;
  ; @usage
  ;  (defn my-form-element [editor-id] [:div ...])
  ;  [item-editor/body :my-editor {:form-element #'my-form-element}]
  [editor-id body-props]
  (let [body-props (body.prototypes/body-props-prototype editor-id body-props)]
       (reagent/lifecycles (core.helpers/component-id editor-id :body)
                           {:reagent-render         (fn []             [body-structure                 editor-id])
                            :component-did-mount    (fn [] (a/dispatch [:item-editor/body-did-mount    editor-id body-props]))
                            :component-will-unmount (fn [] (a/dispatch [:item-editor/body-will-unmount editor-id]))})))
