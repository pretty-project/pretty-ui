
(ns pretty-inputs.select.prototypes
    (:require [fruits.vector.api                 :as vector]
              [fruits.map.api                 :as map]
              [pretty-inputs.engine.api          :as pretty-inputs.engine]
              [pretty-inputs.select.side-effects :as select.side-effects]
              [pretty-standards.api :as pretty-standards]
              [pretty-subitems.api :as pretty-subitems]
              [pretty-rules.api :as pretty-rules]
              [pretty-properties.api :as pretty-properties]
              [dynamic-props.api :as dynamic-props]))

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
       (-> button (pretty-properties/default-mouse-event-props {:on-click-f on-click-f}))))

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
       (-> icon-button (pretty-properties/default-mouse-event-props {:on-click-f on-click-f}))))

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
  [id _ popup]
  (let [close-popup-f (fn [_] (dynamic-props/update-props! id dissoc :popup-visible?))]
       (-> popup (pretty-properties/default-background-color-props {:fill-color :default})
                 (pretty-properties/default-inner-size-props       {:inner-height :content :inner-width :content})
                 (pretty-properties/default-outer-size-props       {:outer-height :parent :outer-layer :uppermost :outer-width :parent})
                 (assoc :overlay {:fill-color :invert :on-click-f close-popup-f} :on-escape-f close-popup-f))))
                
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
       (-> select-button (pretty-properties/default-mouse-event-props {:on-click-f  on-click-f})
                         (pretty-properties/default-input-value-props {:get-value-f get-value-f}))))

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
  [_ _ option-group]
  (-> option-group (update :option-default  map/reversed-deep-merge {:hover-color :muted :indent {:horizontal :s :vertical :xxs}})
                   (update :option-selected map/reversed-deep-merge {:fill-color  :muted})
                   (pretty-properties/default-input-option-props {:max-selection 1})))

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
        icon-button-prototype-f   (fn [%] (icon-button-prototype   id props %))
        popup-prototype-f         (fn [%] (popup-prototype         id props %))
        select-button-prototype-f (fn [%] (select-button-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-standards/standard-flex-props)
                 (pretty-standards/standard-inner-position-props)
                 (pretty-standards/standard-inner-size-props)
                 (pretty-standards/standard-outer-position-props)
                 (pretty-standards/standard-outer-size-props)
                ;(pretty-rules/auto-align-scrollable-flex)
                 (pretty-rules/auto-set-mounted)
                 (pretty-subitems/ensure-subitem           :popup)
                 (pretty-subitems/subitems<-disabled-state :button :header :icon-button :option-group :popup :select-button)
                 (pretty-subitems/leave-disabled-state     :button :header :icon-button :option-group :popup :select-button)
                 (pretty-subitems/apply-subitem-prototype  :button        button-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :option-group  option-group-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :icon-button   icon-button-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :popup         popup-prototype-f)
                 (pretty-subitems/apply-subitem-prototype  :select-button select-button-prototype-f))))
