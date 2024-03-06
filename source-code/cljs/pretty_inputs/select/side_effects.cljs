
(ns pretty-inputs.select.side-effects
    (:require [dynamic-props.api     :as dynamic-props]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [time.api :as time]
              [pretty-inputs.select.config :as select.config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-popup-visibility!
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id _]
  (dynamic-props/update-props! id update :popup-visible? not))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn auto-close-popup!
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (if-not (pretty-inputs.engine/multiple-input-options-selectable? id props)
          (time/set-timeout! (fn [] (toggle-popup-visibility! id props))
                             (-> select.config/AUTO-CLOSE-POPUP-AFTER))))
