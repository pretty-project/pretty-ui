
(ns templates.item-selector.footer.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- all-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {}
  [_ {:keys []}]
  [elements/toggle ::all-item-selected-button
                   {:disabled? false
                    :outdent   {:horizontal :xxs :right :xs}
                    :content   [elements/icon {:size :s
                                               :icon :check_box
                                               :icon-family :material-icons-outlined}]}])

(defn- no-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {}
  [_ {:keys []}]
  [elements/toggle ::no-item-selected-button
                   {:disabled? false
                    :outdent   {:horizontal :xxs :right :xs}
                    :content   [elements/icon {:size :s
                                               :icon :check_box_outline_blank
                                               :icon-family :material-icons-outlined}]}])

(defn- some-item-selected-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {}
  [_ {:keys []}]
  [elements/toggle ::some-item-selected-button
                   {:disabled? false
                    :outdent   {:horizontal :xxs :right :xs}
                    :content   [elements/icon {:size :s
                                               :icon :indeterminate_check_box
                                               :icon-family :material-icons-outlined}]}])

(defn- handle-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {}
  [selector-id {:keys [all-downloaded-item-selected? any-downloaded-item-selected?] :as footer-props}]
  (cond all-downloaded-item-selected? [all-item-selected-button  selector-id footer-props]
        any-downloaded-item-selected? [some-item-selected-button selector-id footer-props]
        :return                       [no-item-selected-button   selector-id footer-props]))


(defn- discard-selection-button
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {:on-discard-selection (metamorphic-event)
  ;  :selected-item-count (integer)}
  [_ {:keys [on-discard-selection selected-item-count]}]
  [elements/button ::discard-selection-button
                   {:disabled?     (< selected-item-count 1)
                    :font-size     :xs
                    :icon          :close
                    :icon-position :right
                    :label         {:content :n-items-selected :replacements [selected-item-count]}
                    :on-click      on-discard-selection
                    :outdent       {:horizontal :xxs :vertical :xs}}])

(defn footer
  ; @param (keyword) selector-id
  ; @param (map) footer-props
  ; {:on-discard (metamorphic-event)
  ;  :selected-item-count (integer)}
  ;
  ; @usage
  ; [footer :my-selector {...}]
  [selector-id footer-props]

  [:div#t-item-selector--footer]

  [:div {:style {:display "flex" :justify-content "space-between"}}
        [:div]
        [discard-selection-button selector-id footer-props]])
       ;[handle-selection-button  selector-id footer-props]
