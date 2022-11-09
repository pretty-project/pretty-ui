
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.ckeditor5.helpers
    (:require ["@ckeditor/ckeditor5-build-decoupled-document"            :as ckeditor5-build-decoupled-document]
              ["@ckeditor/ckeditor5-clipboard/src/utils/plaintexttohtml" :default plainTextToHtml]))
             ;["@ckeditor/ckeditor5-build-classic" :as ckeditor5-build-classic]



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

;; CHANGED
(defn editor-event! [editor event func]
  ;; editor (js-obj)
  ;; event  (string) (other events here -> https://ckeditor.com/docs/ckeditor5/latest/api/module_engine_view_document-Document.html#events)
  ;; func   (fn) (params depends on the event)
  (let [document (-> editor .-editing .-view .-document)]
    (.on document
         event
         func)))

;; CHANGED
(defn force-plaintext-on-copy [editor]
    (editor-event! editor
     "clipboardInput"
     (fn [event-info data]
       (let [html-processor (-> editor .-data .-htmlProcessor)
             plain-text     (.getData (-> data .-dataTransfer) "text/plain")]
         (set! (.-content data)
               (.toView html-processor
                        (plainTextToHtml plain-text)))))))

(defn on-ready-f
  ; @param (?) editor
  [editor]
  (force-plaintext-on-copy editor)
  (let [parent-node (-> editor .-sourceElement .-parentNode)
        toolbar     (-> editor .-ui .-view .-toolbar .-element)]
       (.append parent-node toolbar)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn ckeditor-config
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [background-colors buttons font-colors]}]
  {:toolbar               buttons
   :font-background-color {:colors background-colors}
   :font-color            {:colors font-colors}})
                          ;:columns 3

  ;:toolbar [:heading ...]
  ;:heading {:options [{:model "paragraph"           :title "Paragraph" :class "ck-heading_paragraph"}
  ;                    {:model "heading1" :view "h1" :title "Custom #1" :class "ck-heading_heading1"}}

(defn ckeditor-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) editor-props
  ;  {}
  ;
  ; @return (map)
  ;  {:editor (?)
  ;   :config (map)
  ;   :data (string)
  ;   :on-blur (function)
  ;   :on-change (function)
  ;   :on-focus (function)}
  [editor-id {:keys [on-blur on-focus on-change value] :as editor-props}]
  {:editor    ckeditor5-build-decoupled-document
   :config    (ckeditor-config editor-id editor-props)
   :data      (str value)
   :on-blur   (fn [event]        (if on-blur   (on-blur    editor-id editor-props)))
   :on-focus  (fn [event]        (if on-focus  (on-focus   editor-id editor-props)))
   :on-change (fn [event editor] (if on-change (on-change  editor-id editor-props (-> editor .getData))))
   :on-ready  (fn [editor]       (on-ready-f editor))})
