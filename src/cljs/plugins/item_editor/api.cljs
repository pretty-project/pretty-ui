
(ns plugins.item-editor.api
    (:require [plugins.item-editor.engine :as engine]
              [plugins.item-editor.views  :as views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.item-editor.engine
(def item-id->new-item?  engine/item-id->new-item?)
(def item-id->form-label engine/item-id->form-label)
(def item-id->item-uri   engine/item-id->item-uri)
(def request-id          engine/request-id)
(def synchronizing?      engine/synchronizing?)
(def new-item?           engine/new-item?)
(def get-description     engine/get-description)
(def get-body-props      engine/get-body-props)
(def get-header-props    engine/get-header-props)
(def get-view-props      engine/get-view-props)

; plugins.item-editor.views
(def undo-delete-button  views/undo-delete-button)
(def edit-copy-button    views/edit-copy-button)
(def cancel-item-button  views/cancel-item-button)
(def delete-item-button  views/delete-item-button)
(def archive-item-button views/archive-item-button)
(def copy-item-button    views/copy-item-button)
(def save-item-button    views/save-item-button)
