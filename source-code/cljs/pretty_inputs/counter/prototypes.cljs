
(ns pretty-inputs.counter.prototypes
    (:require [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-models.api        :as pretty-models]
              [pretty-properties.api    :as pretty-properties]
              [pretty-subitems.api      :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn end-button-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) end-button
  ;
  ; @return (map)
  [id props end-button]
  (let [on-click-f (fn [] (pretty-inputs.engine/update-input-value! id props inc))]
       (-> end-button (pretty-properties/default-border-props      {:border-color :muted :border-radius {:all :l} :border-width :xs})
                      (pretty-properties/default-mouse-event-props {:on-click-f on-click-f})
                      (assoc-in [:icon :icon-name] :add))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) label
  ;
  ; @return (map)
  [id props label]
  (let [internal-value (pretty-inputs.engine/get-input-internal-value id props)]
       (-> label (pretty-properties/default-content-props    {:content internal-value})
                 (pretty-properties/default-outer-size-props {:min-outer-width :s}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-button-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; @param (map) start-button
  ;
  ; @return (map)
  [id props start-button]
  (let [on-click-f (fn [] (pretty-inputs.engine/update-input-value! id props dec))]
       (-> start-button (pretty-properties/default-border-props      {:border-color :muted :border-radius {:all :l} :border-width :xs})
                        (pretty-properties/default-mouse-event-props {:on-click-f on-click-f})
                        (assoc-in [:icon :icon-name] :remove))))

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
  (let [end-button-prototype-f   (fn [%] (end-button-prototype   id props %))
        label-prototype-f        (fn [%] (label-prototype        id props %))
        start-button-prototype-f (fn [%] (start-button-prototype id props %))]
       (-> props (pretty-properties/default-flex-props        {:orientation :horizontal :gap :xs})
                 (pretty-properties/default-input-value-props {:initial-value 0})
                 (pretty-properties/default-outer-size-props  {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitems         :end-button :label :start-button)
                 (pretty-subitems/apply-subitem-prototype :end-button   end-button-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :label        label-prototype-f)
                 (pretty-subitems/apply-subitem-prototype :start-button start-button-prototype-f))))
