
(ns pretty-inputs.select.prototypes
    (:require [dynamic-props.api                 :as dynamic-props]
              [pretty-inputs.select.side-effects :as select.side-effects]
              [pretty-properties.api             :as pretty-properties]
              [pretty-models.api                  :as pretty-models]
              [pretty-subitems.api               :as pretty-subitems]
              [form-validator.api :as form-validator]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) button
  ;
  ; @return (map)
  [id _ button]
  (let [on-click-f (fn [_] (dynamic-props/update-props! id update :popup-visible? not))]
       (-> button (pretty-properties/merge-event-fn :on-click-f on-click-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) header
  ;
  ; @return (map)
  [id _ header]
  (let [option-group-id    (pretty-subitems/subitem-id id :option-group)
        option-group-error (form-validator/get-input-error option-group-id)]
       (-> header (assoc-in [:error-text :content] option-group-error))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn icon-button-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) icon-button
  ;
  ; @return (map)
  [id _ icon-button]
  (let [on-click-f (fn [_] (dynamic-props/update-props! id update :popup-visible? not))]
       (-> icon-button (pretty-properties/merge-event-fn :on-click-f on-click-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) option-group
  ;
  ; @return (map)
  [id props option-group]
  (let [on-selected-f (fn [_] (select.side-effects/auto-close-popup! id props))]
       (-> option-group (pretty-properties/default-input-option-props {:option-default  {:hover-color :muted :indent {:horizontal :s :vertical :xxs}}})
                        (pretty-properties/default-input-option-props {:option-selected {:fill-color  :muted}})
                        (pretty-properties/default-input-option-props {:max-selection 1})
                        (pretty-properties/merge-event-fn :on-selected-f on-selected-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn overlay-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) overlay
  ;
  ; @return (map)
  [id _ overlay]
  (let [on-click-f (fn [_] (dynamic-props/update-props! id dissoc :popup-visible?))]
       (-> overlay (pretty-properties/default-background-color-props {:fill-color :default})
                   (pretty-properties/merge-event-fn :on-click-f on-click-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) popup
  ;
  ; @return (map)
  [id props popup]
  (let [on-escape-f         (fn [_] (dynamic-props/update-props! id dissoc :popup-visible?))
        overlay-prototype-f (fn [%] (overlay-prototype           id props %))]
       (-> popup (pretty-properties/default-background-color-props {:fill-color :default})
                 (pretty-properties/default-inner-size-props       {:inner-height :content :inner-width :content})
                 (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-layer :uppermost :outer-width :parent})
                 (pretty-properties/merge-event-fn        :on-escape-f on-escape-f)
                 (pretty-subitems/ensure-subitems         :overlay)
                 (pretty-subitems/apply-subitem-prototype :overlay overlay-prototype-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) select-button
  ;
  ; @return (map)
  [id props select-button]
  (let [on-click-f  (fn [_] (dynamic-props/update-props! id update :popup-visible? not))
        get-value-f (-> props :option-group :get-value-f)]
       (-> select-button (pretty-properties/default-input-value-props {:get-value-f get-value-f})
                         (pretty-properties/merge-event-fn :on-click-f on-click-f))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (let [button-prototype-f        (fn [%] (button-prototype        id props %))
        option-group-prototype-f  (fn [%] (option-group-prototype  id props %))
        header-prototype-f        (fn [%] (header-prototype        id props %))
        icon-button-prototype-f   (fn [%] (icon-button-prototype   id props %))
        popup-prototype-f         (fn [%] (popup-prototype         id props %))
        select-button-prototype-f (fn [%] (select-button-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitems          :popup)
                 (pretty-subitems/subitems<-disabled-state :button :header :icon-button :option-group :popup :select-button)
                 (pretty-subitems/apply-subitem-prototype  :button        button-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :option-group  option-group-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :header        header-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :icon-button   icon-button-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :popup         popup-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :select-button select-button-prototype-f))))
