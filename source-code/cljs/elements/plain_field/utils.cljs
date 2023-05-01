
(ns elements.plain-field.utils
    (:require [dom.api                           :as dom]
              [elements.plain-field.config       :as plain-field.config]
              [elements.plain-field.env          :as plain-field.env]
              [elements.plain-field.side-effects :as plain-field.side-effects]
              [elements.plain-field.state        :as plain-field.state]
              [hiccup.api                        :as hiccup]
              [re-frame.api                      :as r]
              [reagent.api                       :as reagent]
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
  ; After the field unmounted its content and output have to removed from the state
  ; otherwise next time when the field remounts its previous content and output could
  ; affect it.
  ; + It has to be checked whether the field is still in the DOM-tree because it might be
  ;   in case of the field (with the same ID) is duplicated on the page.
  ;   Its pretty common to use duplicated fields by displaying a field on a page of a
  ;   content swapper and the same field on another page of that very content swapper.
  ;   And during the content swapper changing pages animatedly there is short overlap
  ;   in the lifetimes of the duplicated fields.
  (letfn [(f [] (let [input-id      (hiccup/value field-id "input")
                      input-element (dom/get-element-by-id input-id)]
                    (when-not input-element (plain-field.side-effects/set-field-content! field-id nil)
                                            (plain-field.side-effects/set-field-output!  field-id nil))))]
         (time/set-timeout! f 50)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-changed-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id _]
  (let [timestamp (time/elapsed)]
       (swap! plain-field.state/FIELD-STATES assoc field-id {:changed-at timestamp})))

(defn resolve-field-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  ; The 'resolve-field-change!' function is called by the 'on-change-f' function
  ; with a timeout after the field's content has been changed.
  ; If the field's content hasn't changed again during the timeout, ...
  ; ... the type considered as ended.
  ; ... the application state gets updated with the field's content.
  ; ... the :on-type-ended event being dispatched.
  ;
  ; This function doesn't takes the field's content as its argument, because
  ; it's called with a timeout and the field's content can changes again during
  ; the timeout.
  (let [timestamp  (time/elapsed)
        changed-at (get-in @plain-field.state/FIELD-STATES [field-id :changed-at])]
       (when (> timestamp (+ changed-at plain-field.config/TYPE-ENDED-AFTER))
             (r/dispatch-sync [:elements.plain-field/type-ended field-id field-props]))))

(defn on-change-f
  ; @ignore
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ; {:modifier (function)(opt)
  ;  :on-changed (Re-Frame metamorphic-event)(opt)}
  ; @param (DOM-event) event
  ;
  ; @return (function)
  [field-id {:keys [modifier on-changed] :as field-props} event]
  (let [field-content (if modifier (-> event dom/event->value modifier)
                                   (-> event dom/event->value))]
       (field-changed-f field-id field-props)
       (plain-field.side-effects/set-field-content! field-id field-content)
       (letfn [(f [] (resolve-field-change-f field-id field-props))]
              (time/set-timeout! f plain-field.config/TYPE-ENDED-AFTER))
       (if on-changed (let [on-changed (r/metamorphic-event<-params on-changed field-content)]
                           (r/dispatch-sync on-changed)))))
