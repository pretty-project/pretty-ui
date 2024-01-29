
(ns pretty-state.input-presets
    (:require [fruits.vector.api  :as vector]
              [pretty-presets.api :refer [reg-preset!]]
              [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(reg-preset! :pretty-state/field-input
  ; @ignore
  ;
  ; @param (map) input-props
  ; {}
  ;
  ; @return (map)
  ; {}
  (fn [{:keys [options options-path value-path] :as input-props}]

      ; Emiatt a blur esemény azonnal a state-be irta a field content-et
      ; de most már a validator-ok nem a state alapján validálnak hanem az internal-value alapján
      ; viszont still van olyan eset, ahol kéne, hogy azonnal a state-be irodjon a field értéke
      ; pl.: egy submit gombra kattintás okozza a field elhagyását akkor azonnal kéne a state-ben a content!
      ;
      ; - When the user leaves a field it writes its actual field content into the Re-Frame state immediately.
      ; - Normally that state-writing action happens delayed after the last key is pressed
      ;   but there are cases when it must happen immediately.
      ;   E.g., The user ends typing and quickly clicks on a button that triggers the validation
      ;         of the field or a validation of a form that contains the field.
      ;         When the validator functions are getting applied on the stored value of
      ;         the field (stored in the Re-Frame state) it's important to the state
      ;         contains the actual field content!
      ;         The solution is that the 'on-mouse-down' event of the button fires the
      ;         'on-blur' event of the field and that event writes the field content into
      ;         the state immediately and when the 'on-mouse-up' event of the button
      ;         triggers the validation, the actual field content will be already in the application state.
      (merge (if options      {:get-options-f   #(-> options)})
             (if options-path {:get-options-f   #(r/subscribed [:get-item  options-path])})
             (if value-path   {:get-value-f     #(r/subscribed [:get-item  value-path])})
             (if value-path   {:on-type-ended-f #(r/dispatch   [:set-item! value-path %])})
             (if value-path   {:on-blur-f       #(r/dispatch   [:set-item! value-path %])})
             (-> input-props))))

(reg-preset! :pretty-state/selectable-input
  ; @ignore
  ;
  ; @param (map) input-props
  ; {}
  ;
  ; @return (map)
  ; {}
  (fn [{:keys [add-options? options options-path value-path] :as input-props}]
      (merge (if options      {:get-options-f #(-> options)})
             (if add-options? {:add-option-f  #(r/dispatch   [:update-item! options-path vector/cons-item-once %])})
             (if options-path {:get-options-f #(r/subscribed [:get-item     options-path])})
             (if value-path   {:get-value-f   #(r/subscribed [:get-item     value-path])})
             (if value-path   {:set-value-f   #(r/dispatch   [:set-item!    value-path %])})
             (-> input-props))))
