
(ns ckeditor5.helpers
    (:require ["@ckeditor/ckeditor5-clipboard/src/utils/plaintexttohtml" :default plainTextToHtml]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn editor-event
  ; @ignore
  ;
  ; @param (?) editor
  ; @param (string) event
  ; https://ckeditor.com/docs/ckeditor5/latest/api/module_engine_view_document-Document.html#events
  ; @param (function) f
  [editor event f]
  (let [document (-> editor .-editing .-view .-document)]
       (.on document event f)))

(defn force-paste-as-plain-text
  ; @ignore
  ;
  ; @param (?) editor
  [^js editor]
  ; BUG#0031
  ; https://cljs.github.io/api/syntax/js-tag?fbclid=IwAR3qcZ4f96cU3uALMpS4caUVHb0BbIf0j7u-j0-t6QxMF1vTRkF4GVit9v4
  ; - When the Shadow-CLJS gets the {:optimizations :advanced} setting, it minifies
  ;   the JS files very much.
  ; - It doesn't minifies the name of the editor.data.processor object's .toView function,
  ;   even though the force-paste-as-plain-text function tries the call the .toView
  ;   with a minified name.
  ; - By using the ^js tag, the compiler knows that the functions in the editor
  ;   object don't available on minified names.
  (letfn [(f [event-info data]
             (let [html-processor (-> editor .-data .-processor)
                  ;html-processor (-> editor .-data .-htmlProcessor)
                   plain-text     (.getData (-> data .-dataTransfer) "text/plain")]
                  (set! (.-content data)
                        (.toView html-processor (plainTextToHtml plain-text)))))]
         (editor-event editor "clipboardInput" f)))

(defn on-ready-f
  ; @ignore
  ;
  ; @param (?) editor
  [editor]
  (force-paste-as-plain-text editor)
  (let [parent-node (-> editor .-sourceElement .-parentNode)
        toolbar     (-> editor .-ui .-view .-toolbar .-element)]
       (.append parent-node toolbar)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ckeditor-config
  ; @ignore
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [_ {:keys [fill-colors buttons font-colors]}]
  {:toolbar               buttons
   :font-background-color {:colors fill-colors}
   :font-color            {:colors font-colors}})
                          ;:columns 3

  ; :toolbar [:heading ...]
  ; :heading {:options [{:model "paragraph"           :title "Paragraph" :class "ck-heading_paragraph"}
  ;                     {:model "heading1" :view "h1" :title "Custom #1" :class "ck-heading_heading1"}]
