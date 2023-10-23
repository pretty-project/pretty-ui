
(ns elements.plain-field.utils
    (:require [dom.api                           :as dom]
              [elements.plain-field.config       :as plain-field.config]
              [elements.plain-field.env          :as plain-field.env]
              [elements.plain-field.side-effects :as plain-field.side-effects]
              [elements.plain-field.state        :as plain-field.state]
              [hiccup.api                        :as hiccup]
              [noop.api                          :refer [return]]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]
              [string.api                        :as string]
              [time.api                          :as time]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn synchronizer-did-mount-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:field-content-f (function)
  ;  :value-path (Re-Frame path vector)}
  [field-id {:keys [field-content-f value-path]}]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  (let [stored-value  @(r/subscribe [:get-item value-path])
        stored-content (field-content-f stored-value)]
       (plain-field.side-effects/set-field-content! field-id stored-content)))

       ; HACK#9760
       ; This was an experimental solution to avoid flickering of input contents.
       ; (letfn [(f [] (set-field-content! field-id stored-content))]
       ;        (time/set-timeout! f 350)]))

(defn synchronizer-did-update-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (?) %
  [field-id %]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  ; When the stored value (in the application state) gets updated, checks
  ; whether if it's different from the field content.
  ; If it's different, updates the field content with the content derived from
  ; the stored value.
  (let [[_ {:keys [field-content-f] :as field-props} stored-value] (reagent/arguments %)]
       (let [stored-content (field-content-f stored-value)]
            (when (not= stored-content (plain-field.env/get-field-content field-id))
                  (plain-field.side-effects/set-field-content! field-id stored-content)))))

(defn synchronizer-will-unmount-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  ; HACK#9910 (source-code/cljs/elements/plain_field/views.cljs)
  ;
  ; After the field unmounted, its content and output have to removed from the state
  ; otherwise next time when the field remounts its previous content and output could
  ; affect it.
  ; + It has to be checked whether the field is still mounted to the DOM-tree because
  ;   it might be mounted if the field is duplicated on the page (with the same ID).
  ;   It's pretty common to use duplicated fields by displaying a field on a page of a
  ;   content swapper and the same field on another page of that very content swapper.
  ;   And during the content swapper animated page changing process, there is short overlap
  ;   in the lifetimes of that fields.
  (letfn [(f [] (let [input-id      (hiccup/value field-id "input")
                      input-element (dom/get-element-by-id input-id)]
                     (when-not input-element (plain-field.side-effects/set-field-content! field-id nil)
                                             (plain-field.side-effects/set-field-output!  field-id nil))))]
         (time/set-timeout! f 50)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-change-event->field-content
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:modifier-f (function)(opt)}
  ; @param (DOM-event) event
  ;
  ; @return (string)
  [_ {:keys [max-length modifier-f type]} event]
  (let [value (if modifier-f (-> event dom/event->value modifier-f)
                             (-> event dom/event->value))]
       ; https://stackoverflow.com/questions/9555143/html-maxlength-attribute-not-working-on-chrome-and-safari
       (if (and max-length (= type :number))
           (string/max-length value max-length)
           (return            value))))

(defn resolve-field-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; The 'resolve-field-change!' function is called by the 'on-change-f' function
  ; with a delay after the field's content has been changed.
  ; If the field's content hasn't changed during the delay, ...
  ; ... the typing is considered as ended.
  ; ... the application state gets updated with the field's content.
  ; ... the :on-type-ended event is being dispatched.
  ;
  ; This function doesn't take the field's content as its argument, because
  ; it's called with a delay and the field's content can change during that time.
  (when (plain-field.env/type-ended? field-id)
        (r/dispatch-sync [:elements.plain-field/type-ended field-id field-props])))

(defn field-changed-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:on-changed (Re-Frame metamorphic-event)(opt)}
  ; @param (string) field-content
  [field-id {:keys [on-changed] :as field-props} field-content]
  (let [timestamp (time/elapsed)]
       (swap! plain-field.state/FIELD-STATES assoc field-id {:changed-at timestamp})
       (plain-field.side-effects/set-field-content! field-id field-content)
       (letfn [(f [] (resolve-field-change-f field-id field-props))]
              (time/set-timeout! f plain-field.config/TYPE-ENDED-AFTER))
       (if on-changed (let [on-changed (r/metamorphic-event<-params on-changed field-content)]
                           (r/dispatch-sync on-changed)))))

(defn on-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; @param (DOM-event) event
  [field-id field-props event]
  (let [field-content (on-change-event->field-content field-id field-props event)]
       (field-changed-f field-id field-props field-content)))
