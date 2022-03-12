
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.core.views
    (:require [app-fruits.reagent                 :as reagent]
              [plugins.item-editor.core.helpers   :as core.helpers]
              [plugins.item-editor.engine.helpers :as engine.helpers]
              [mid-fruits.string                  :as string]
              [x.app-core.api                     :as a]
              [x.app-elements.api                 :as elements]
              [x.app-layouts.api                  :as layouts]))



;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/delete-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/error-mode?      extension-id item-namespace])]
       [elements/button ::delete-item-button
                        {:tooltip :delete! :preset :delete-icon-button
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/delete-item! extension-id item-namespace]}]))

(defn copy-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/copy-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/error-mode?      extension-id item-namespace])]
       [elements/button ::copy-item-button
                        {:tooltip :duplicate! :preset :duplicate-icon-button
                         :disabled? (or editor-disabled? error-mode?)
                         :on-click  [:item-editor/duplicate-item! extension-id item-namespace]}]))

(defn save-item-button
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/save-item-button :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])
        error-mode?      @(a/subscribe [:item-editor/error-mode?      extension-id item-namespace])
        form-completed?  @(a/subscribe [:item-editor/form-completed?  extension-id item-namespace])]
       [elements/button ::save-item-button
                        {:tooltip :save! :preset :save-icon-button
                         :disabled? (or (not form-completed?) editor-disabled? error-mode?)
                         :on-click [:item-editor/save-item! extension-id item-namespace]}]))



;; -- Form components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn new-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [new-item-label (engine.helpers/new-item-label extension-id item-namespace)]
       [elements/label ::new-item-label
                       {:content new-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn named-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:name (metamorphic-content)}
  [extension-id item-namespace {:keys [name]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/label ::named-item-label
                       {:content name :font-weight :extra-bold :font-size :l
                        :color (if editor-disabled? :highlight :default)}]))

(defn unnamed-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [unnamed-item-label (engine.helpers/unnamed-item-label extension-id item-namespace)]
       [elements/label ::unnamed-item-label
                       {:content unnamed-item-label :color :highlight :font-weight :extra-bold :font-size :l}]))

(defn item-label
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) element-props
  ;  {:name (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/item-label :my-extension :my-type {...}]
  [extension-id item-namespace {:keys [name] :as element-props}]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       (cond (string/nonempty? name)      [named-item-label   extension-id item-namespace element-props]
             (boolean          new-item?) [new-item-label     extension-id item-namespace]
             :unnamed-item                [unnamed-item-label extension-id item-namespace])))



;; -- Input components --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/input-group-header :my-extension :my-type {...}]
  [extension-id item-namespace {:keys [label]}]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [layouts/input-group-header {:label label :color (if editor-disabled? :highlight :default)}]))

(defn description-field
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ;
  ; @usage
  ;  [item-editor/description-field :my-extension :my-type]
  [extension-id item-namespace]
  (let [editor-disabled? @(a/subscribe [:item-editor/editor-disabled? extension-id item-namespace])]
       [elements/multiline-field ::description-field
                                 {:value-path [extension-id :item-editor/data-items :description]
                                  :disabled?  editor-disabled?}]))



;; -- Header components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-start-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       [:<>                   [save-item-button extension-id item-namespace]
            (if-not new-item? [copy-item-button extension-id item-namespace])]))

(defn menu-end-buttons
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (let [new-item? @(a/subscribe [:item-editor/new-item? extension-id item-namespace])]
       (if-not new-item? [delete-item-button extension-id item-namespace])))

(defn menu-mode-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/horizontal-polarity {:start-content [menu-start-buttons extension-id item-namespace]
                                 :end-content   [menu-end-buttons   extension-id item-namespace]}])

(defn- header-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if-let [menu-element @(a/subscribe [:item-editor/get-menu-element extension-id item-namespace])]
          [menu-element     extension-id item-namespace]
          [menu-mode-header extension-id item-namespace]))

(defn header
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) header-props
  ;  {:item-actions (keywords in vector)(opt)
  ;    [:delete, :duplicate, :save]
  ;   :menu-element (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [item-editor/header :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-menu-element [extension-id item-namespace] [:div ...])
  ;  [item-editor/header :my-extension :my-type {:menu #'my-menu-element}]
  [extension-id item-namespace header-props]
  (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :header)
                      {:reagent-render      (fn []             [header-structure          extension-id item-namespace])
                       :component-did-mount (fn [] (a/dispatch [:item-editor/init-header! extension-id item-namespace header-props]))}))



;; -- Indicator components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn downloading-item-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/label {:font-size :xs :color :highlight :font-weight :bold
                   :content :downloading...}])

(defn downloading-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  [elements/row {:content [downloading-item-label extension-id item-namespace]
                 :horizontal-align :center}])



;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [_ _]
  [:<> ;[elements/horizontal-separator {:size :xxl}]
       [elements/label {:min-height :m :content :an-error-occured :font-size :m}]
       [elements/label {:min-height :m :content :the-item-you-opened-may-be-broken :color :muted}]])

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  [extension-id item-namespace]
  (if-let [error-mode? @(a/subscribe [:item-editor/error-mode? extension-id item-namespace])]
          [error-body extension-id item-namespace]
          (if-let [data-received? @(a/subscribe [:item-editor/get-meta-item extension-id item-namespace :data-received?])]
                  (if-let [form-element @(a/subscribe [:item-editor/get-form-element extension-id item-namespace])]
                          [form-element extension-id item-namespace])
                  [downloading-item extension-id item-namespace])))

(defn body
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) body-props
  ;  {:form-element (metamorphic-content)
  ;   :handler-key (keyword)
  ;   :item-id (string)
  ;   :new-item-id (string)(opt)
  ;    Default: false
  ;   :suggestion-keys (keywords in vector)(opt)}
  ;
  ; @usage
  ;  [item-editor/body :my-extension :my-type {...}]
  ;
  ; @usage
  ;  (defn my-form-element [extension-id item-namespace] [:div ...])
  ;  [item-editor/body :my-extension :my-type {:form-element #'my-form-element}]
  [extension-id item-namespace body-props]
  (let [body-props (body-props-prototype extension-id item-namespace body-props)]
       (reagent/lifecycles (core.helpers/component-id extension-id item-namespace :body)
                           {:reagent-render         (fn []             [body-structure              extension-id item-namespace])
                            :component-will-unmount (fn [] (a/dispatch [:item-editor/destruct-body! extension-id item-namespace]))
                            :component-did-mount    (fn [] (a/dispatch [:item-editor/init-body!     extension-id item-namespace body-props]))
                            :component-did-update   (fn [this _] (let [] (println (str (reagent/arguments this)))))})))

                            ; Az updater alkalmazásával az elem törlése utáni átirányításkor a megváltozott route-ra
                            ; feliratkozott item-lister/body komponens megpróbál újratölteni kilépés közben!
